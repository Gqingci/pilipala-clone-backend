package com.pilipala.web.aspect;

import com.pilipala.web.annotation.GlobalInterceptor;
import com.pilipala.component.RedisComponent;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.exception.BusinessException;
import com.pilipala.utils.RedisConstants;
import com.pilipala.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class GlobalOperationAspect {
    @Resource
    private RedisComponent redisComponent;

    @Before("@annotation(com.pilipala.web.annotation.GlobalInterceptor)")
    public void intercept(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if(interceptor == null) {
            return;
        }
        
        if(interceptor.checkLogin()) {
            checkLogin();
        }
    }

    private void checkLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(RedisConstants.TOKEN_WEB);
        if(StringUtils.isEmpty( token)) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        UserTokenInfoDTO userTokenInfoDTO = redisComponent.getUserTokenInfoDTO(token);
        if(userTokenInfoDTO == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
    }
}
