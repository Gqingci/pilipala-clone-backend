package com.pilipala.web.controller;

import com.pilipala.annotation.RecordUserMessage;
import com.pilipala.entity.enums.MessageTypeEnum;
import com.pilipala.web.annotation.GlobalInterceptor;
import com.pilipala.entity.po.UserAction;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.UserActionService;
import com.pilipala.utils.Constants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.*;
import java.util.Date;

@RestController
@RequestMapping("/userAction")
public class UserActionController extends ABaseController {

    @Resource
    private UserActionService userActionService;

    @RequestMapping("/doAction")
    @GlobalInterceptor(checkLogin = true)
    @RecordUserMessage(messageType = MessageTypeEnum.LIKE)
    public ResponseVO doAction(@NotEmpty String videoId,
                               @NotNull Integer actionType,
                               @Min(1) @Max(2) Integer actionCount,
                               Integer commentId) {
        UserAction userAction = new UserAction();
        userAction.setVideoId(videoId);
        userAction.setActionType(actionType);
        userAction.setActionTime(new Date());
        userAction.setUserId(getUserTokenInfoDTO().getId());
        if (actionCount != null && (actionCount == 1 || actionCount == 2)) {
            userAction.setActionCount(actionCount);
        } else {
            actionCount = Constants.ONE;
        }
        userAction.setActionCount(actionCount);
        commentId = commentId == null ? Constants.ZERO : commentId;
        userAction.setCommentId(commentId);
        userActionService.saveAction(userAction);

        return getSuccessResponseVo(null);
    }
}
