package com.pilipala.web.controller;

import com.pilipala.dto.UserMessageCountDTO;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.MessageReadTypeEnum;
import com.pilipala.entity.po.UserMessage;
import com.pilipala.entity.query.UserMessageQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.UserMessageService;
import com.pilipala.web.annotation.GlobalInterceptor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequestMapping("/message")
public class UserMessageController extends ABaseController {
    @Resource
    private UserMessageService userMessageService;

    @RequestMapping("/getNoReadCount")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getNoReadCount() {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UserMessageQuery query = new UserMessageQuery();
        query.setUserId(userTokenInfoDTO.getId());
        query.setReadType(MessageReadTypeEnum.NO_READ.getType());
        Integer count = userMessageService.findCountByParam(query);
        return getSuccessResponseVo(count);
    }

    @RequestMapping("getNoReadCountGroup")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getNoReadCountGroup() {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        List<UserMessageCountDTO> dataList = userMessageService.getMessageTypeNoReadCount(userTokenInfoDTO.getId());
        return getSuccessResponseVo(dataList);
    }

    @RequestMapping("readAll")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO readAll(@NotNull Integer messageType) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UserMessageQuery query = new UserMessageQuery();
        query.setUserId(userTokenInfoDTO.getId());
        query.setMessageType(messageType);
        UserMessage userMessage = new UserMessage();
        userMessage.setReadType(MessageReadTypeEnum.READ.getType());
        userMessageService.updateByParam(userMessage, query);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("loadMessage")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadMessage(@NotNull Integer messageType, Integer pageNo) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UserMessageQuery query = new UserMessageQuery();
        query.setUserId(userTokenInfoDTO.getId());
        query.setMessageType(messageType);
        query.setPageNo(pageNo);
        query.setOrderBy("message_id desc");
        PaginationResultVO resultVO = userMessageService.findListByPage(query);
        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("delMessage")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delMessage(@NotNull Integer messageId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UserMessageQuery query = new UserMessageQuery();
        query.setUserId(userTokenInfoDTO.getId());
        query.setMessageId(messageId);
        userMessageService.deleteByParam(query);
        return getSuccessResponseVo(null);
    }
}
