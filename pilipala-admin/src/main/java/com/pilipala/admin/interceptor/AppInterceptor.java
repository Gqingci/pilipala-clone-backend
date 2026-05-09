package com.pilipala.admin.interceptor;


import com.pilipala.component.RedisComponent;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.exception.BusinessException;
import com.pilipala.utils.RedisConstants;
import com.pilipala.utils.StringUtils;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AppInterceptor implements HandlerInterceptor {
    private final static String URL_ACCOUNT = "/account";
    private final static String URL_FILE = "/file";

    @Resource
    private RedisComponent redisComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (request.getRequestURI().contains(URL_ACCOUNT)) {
            return true;
        }

        String token = request.getHeader(RedisConstants.TOKEN_ADMIN);
        // 获取图片
        if (request.getRequestURI().contains(URL_FILE)) {
            token = getTokenFromCookie(request);
        }
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }

        Object sessionObj = redisComponent.getTokenInfo4Admin(token);
        if (sessionObj == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        return true;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(RedisConstants.TOKEN_ADMIN)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
