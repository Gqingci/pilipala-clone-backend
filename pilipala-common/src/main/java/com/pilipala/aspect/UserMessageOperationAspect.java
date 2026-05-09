package com.pilipala.aspect;

import com.pilipala.annotation.RecordUserMessage;
import com.pilipala.component.RedisComponent;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.MessageTypeEnum;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.UserMessageService;
import com.pilipala.utils.RedisConstants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
public class UserMessageOperationAspect {
    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UserMessageService userMessageService;

    private static final String PARAMETERS_VIDEO_ID = "videoId";

    private static final String PARAMETERS_ACTION_TYPE = "actionType";

    private static final String PARAMETERS_REPLY_COMMENT_ID = "replyCommentId";

    private static final String PARAMETERS_AUDIT_REJECT_REASON = "reason";

    private static final String PARAMETERS_CONTENT = "content";

    @Around("@annotation(com.pilipala.annotation.RecordUserMessage)")
    public ResponseVO intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseVO responseVO = (ResponseVO) joinPoint.proceed();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RecordUserMessage recordUserMessage = method.getAnnotation(RecordUserMessage.class);
        if (recordUserMessage != null) {
            saveMessage(recordUserMessage, joinPoint.getArgs(), method.getParameters());
        }
        return responseVO;

    }

    private void saveMessage(RecordUserMessage recordUserMessage, Object[] args, Parameter[] parameters) {
        String videoId = null;
        Integer actionType = null;
        Integer replyCommentId = null;
        String content = null;
        for (int i = 0; i < parameters.length; i++) {
            if (PARAMETERS_VIDEO_ID.equals(parameters[i].getName())) {
                videoId = (String) args[i];
            } else if (PARAMETERS_ACTION_TYPE.equals(parameters[i].getName())) {
                actionType = (Integer) args[i];
            } else if (PARAMETERS_REPLY_COMMENT_ID.equals(parameters[i].getName())) {
                replyCommentId = (Integer) args[i];
            } else if (PARAMETERS_CONTENT.equals(parameters[i].getName())) {
                content = (String) args[i];
            } else if (PARAMETERS_AUDIT_REJECT_REASON.equals(parameters[i].getName())) {
                content = (String) args[i];
            }
        }
        MessageTypeEnum messageType = recordUserMessage.messageType();
        if(UserActionTypeEnum.VIDEO_COLLECT.getType().equals(actionType)){
            messageType = MessageTypeEnum.COLLECTION;
        }

        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        userMessageService.saveUserMessage(videoId,
                userTokenInfoDTO == null ? null : userTokenInfoDTO.getId(),
                messageType,
                content,
                replyCommentId);
    }

    private UserTokenInfoDTO getUserTokenInfoDTO() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(RedisConstants.TOKEN_WEB);
        return redisComponent.getUserTokenInfoDTO(token);
    }
}
