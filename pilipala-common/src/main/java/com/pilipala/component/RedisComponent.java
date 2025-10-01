package com.pilipala.component;

import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.redis.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

import com.pilipala.utils.RedisConstants;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public String saveRegisterCaptcha(String text) {
        String captchaKey = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_REGISTER_CAPTCHA_KEY + captchaKey, text, RedisConstants.CAPTCHA_TTL);
        return captchaKey;
    }

    public String saveLoginCaptcha(String text) {
        String captchaKey = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_LOGIN_CAPTCHA_KEY + captchaKey, text, RedisConstants.CAPTCHA_TTL);
        return captchaKey;
    }

    public String getRegisterCaptcha(String captchaKey) {
        return (String) redisUtils.get(RedisConstants.REDIS_REGISTER_CAPTCHA_KEY + captchaKey);
    }

    public String getLoginCaptcha(String captchaKey) {
        return (String) redisUtils.get(RedisConstants.REDIS_LOGIN_CAPTCHA_KEY + captchaKey);
    }

    public void cleanRegisterCaptcha(String captchaKey) {
        redisUtils.delete(RedisConstants.REDIS_REGISTER_CAPTCHA_KEY + captchaKey);
    }

    public void cleanLoginCaptcha(String captchaKey) {
        redisUtils.delete(RedisConstants.REDIS_LOGIN_CAPTCHA_KEY + captchaKey);
    }

    public void saveToken(UserTokenInfoDTO userTokenInfoDTO) {
        String token = UUID.randomUUID().toString();
        userTokenInfoDTO.setExpireAt(System.currentTimeMillis() + RedisConstants.LOGIN_USER_TTL);
        userTokenInfoDTO.setToken(token);
        redisUtils.setex(RedisConstants.REDIS_WEB_TOKEN_KEY + token, userTokenInfoDTO, RedisConstants.LOGIN_USER_TTL);
    }

    public void cleanToken(String token) {
        redisUtils.delete(RedisConstants.REDIS_WEB_TOKEN_KEY + token);
    }

    public UserTokenInfoDTO getUserTokenInfoDTO(String token) {
        return (UserTokenInfoDTO) redisUtils.get(RedisConstants.REDIS_WEB_TOKEN_KEY + token);
    }

    public String saveToken4Admin(String account) {
        String token = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_ADMIN_TOKEN_KEY + token, account, RedisConstants.REDIS_KEY_EXPIRES_ONE_DAY);
        return token;
    }

    public void cleanToken4Admin(String token) {
        redisUtils.delete(RedisConstants.REDIS_ADMIN_TOKEN_KEY + token);
    }

    public String getLoginCaptcha4Admin(String captchaKey) {
        return (String) redisUtils.get(RedisConstants.REDIS_ADMIN_CAPTCHA_KEY + captchaKey);
    }

    public void cleanLoginCaptcha4Admin(String captchaKey) {
        redisUtils.delete(RedisConstants.REDIS_ADMIN_CAPTCHA_KEY + captchaKey);
    }

    public String saveLoginCaptcha4Admin(String text) {
        String captchaKey = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_ADMIN_CAPTCHA_KEY + captchaKey, text, RedisConstants.CAPTCHA_TTL);
        return captchaKey;
    }
}
