package com.pilipala.web.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.pilipala.dto.UserCountInfoDTO;
import com.pilipala.web.annotation.GlobalInterceptor;
import com.pilipala.component.RedisComponent;
import com.pilipala.dto.LoginFormDTO;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.exception.BusinessException;
import com.pilipala.utils.Constants;
import com.pilipala.utils.RedisConstants;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.ByteArrayOutputStream;

import com.pilipala.service.UsersService;
import com.pilipala.entity.vo.ResponseVO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gqingci
 * @Description: 的Controller类
 * @date: 2025/09/15
 */

@RestController
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController {
    @Resource
    private UsersService usersService;
    @Resource
    private DefaultKaptcha captchaProducer;
    @Resource
    private RedisComponent redisComponent;

    private static final int DAILY_LOGIN_REWARD = 5;

    @RequestMapping("/captcha")
    @GlobalInterceptor
    public ResponseVO captcha(@RequestParam(defaultValue = "register") String type,
                              HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成验证码文本
        String text = captchaProducer.createText();
        String captchaKey;

        // 根据 type 选择前缀
        if ("login".equalsIgnoreCase(type)) {
            captchaKey = redisComponent.saveLoginCaptcha(text);
        } else { // 默认注册
            captchaKey = redisComponent.saveRegisterCaptcha(text);
        }

        // 生成图片
        BufferedImage image = captchaProducer.createImage(text);

        // 转成 Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        String base64Img = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());

        Map<String, String> result = new HashMap<>();
        result.put("captcha", base64Img);
        result.put("captchaKey", captchaKey);

        return getSuccessResponseVo(result);
    }

    @RequestMapping("/register")
    @GlobalInterceptor
    public ResponseVO register(@NotEmpty @Email @Size(max = 128) String email,
                               @NotEmpty @Pattern(regexp = Constants.PASSWORD_REGEXP) String registerPassword,
                               @NotEmpty String captchaKey,
                               @NotEmpty String captcha) {
        try {
            if (!captcha.equalsIgnoreCase(redisComponent.getRegisterCaptcha(captchaKey))) {
                throw new BusinessException("验证码错误。");
            }
            usersService.register(email, registerPassword);
            return getSuccessResponseVo(null);
        } finally {
            redisComponent.cleanRegisterCaptcha(captchaKey);
        }
    }

    @RequestMapping("/login")
    @GlobalInterceptor
    public ResponseVO login(HttpServletRequest request,
                            HttpServletResponse response,
                            @NotEmpty @Email String email,
                            @NotEmpty String password,
                            @NotEmpty String captchaKey,
                            @NotEmpty String captcha) {
        try {
            if (!captcha.equalsIgnoreCase(redisComponent.getLoginCaptcha(captchaKey))) {
                throw new BusinessException("验证码错误。");
            }
            LoginFormDTO loginForm = new LoginFormDTO(email, password, captchaKey, captcha);
            String ip = getClientIp();
            UserTokenInfoDTO userTokenInfoDTO = usersService.login(loginForm, ip);
            saveToken2Cookie(response, userTokenInfoDTO.getToken());

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                String token = null;
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(RedisConstants.TOKEN_WEB)) {
                        token = cookie.getValue();
                    }
                }

                if (token != null) {
                    redisComponent.cleanToken(token);
                }
            }
            giveDailyLoginReward(userTokenInfoDTO.getId());

            return getSuccessResponseVo(userTokenInfoDTO);
        } finally {
            redisComponent.cleanLoginCaptcha(captchaKey);
        }
    }

    @PostMapping("/persistentLogin")
    @GlobalInterceptor
    public ResponseVO persistentLogin(HttpServletResponse response) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        if (userTokenInfoDTO == null) {
            return getSuccessResponseVo(null);
        }
        long ttlLeft = userTokenInfoDTO.getExpireAt() - System.currentTimeMillis();
        if (ttlLeft < RedisConstants.LOGIN_USER_TTL / 3) {
            // 刷新 token（更新 Redis 里的过期时间）
            redisComponent.saveToken(userTokenInfoDTO);
        }

        // 无论是否刷新，都回写一次 Cookie（保证续期）
        saveToken2Cookie(response, userTokenInfoDTO.getToken());

        giveDailyLoginReward(userTokenInfoDTO.getId());

        return getSuccessResponseVo(userTokenInfoDTO);
    }

    @PostMapping("/logout")
    public ResponseVO signOut(HttpServletResponse response) {
        cleanCookie(response);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("getUserCountInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getUserCountInfo() {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UserCountInfoDTO userCountInfoDTO = usersService.getUserCountInfo(userTokenInfoDTO.getId());
        return getSuccessResponseVo(userCountInfoDTO);
    }

    private void giveDailyLoginReward(String userId) {
        String key = "DAILY_LOGIN_REWARD:" + userId + ":" + LocalDate.now();
        // 使用 Redis SETNX 记录今天是否已经发放
        Boolean alreadyRewarded = redisComponent.get(key) != null;
        if (!alreadyRewarded) {
            // 发放奖励
            usersService.addCoins(userId, DAILY_LOGIN_REWARD);
            // 记录奖励状态，设置过期时间为今天结束
            long secondsUntilEndOfDay = LocalDate.now().plusDays(1).atStartOfDay()
                    .toEpochSecond(ZoneOffset.UTC) - Instant.now().getEpochSecond();
            redisComponent.set(key, "1", secondsUntilEndOfDay);
        }
    }
}