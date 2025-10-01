package com.pilipala.admin.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.pilipala.component.RedisComponent;
import com.pilipala.config.AppConfig;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.utils.RedisConstants;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private DefaultKaptcha captchaProducer;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private AppConfig appConfig;

    @RequestMapping("/captcha")
    public ResponseVO captcha(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成验证码文本
        String text = captchaProducer.createText();
        String captchaKey;

        captchaKey = redisComponent.saveLoginCaptcha4Admin(text);

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

    @RequestMapping("/login")
    public ResponseVO login(HttpServletRequest request,
                            HttpServletResponse response,
                            @NotEmpty String account,
                            @NotEmpty String password,
                            @NotEmpty String captchaKey,
                            @NotEmpty String captcha) {
        try {
            if (!captcha.equalsIgnoreCase(redisComponent.getLoginCaptcha4Admin(captchaKey))) {
                throw new BusinessException("验证码错误。");
            }

            if (!account.equals(appConfig.getAdminAccount()) || !password.equals(appConfig.getAdminPassword())) {
                throw new BusinessException("账号或密码错误。");
            }

            String token = redisComponent.saveToken4Admin(account);
            saveToken2Cookie(response, token);

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                String tokenTemp = null;
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(RedisConstants.TOKEN_ADMIN)) {
                        tokenTemp = cookie.getValue();
                    }
                }

                if (token != null) {
                    redisComponent.cleanToken4Admin(token);
                }
            }

            return getSuccessResponseVo(account);
        } finally {
            redisComponent.cleanLoginCaptcha4Admin(captchaKey);
        }
    }

    @PostMapping("/logout")
    public ResponseVO signOut(HttpServletResponse response) {
        cleanCookie(response);
        return getSuccessResponseVo("Success");
    }


}