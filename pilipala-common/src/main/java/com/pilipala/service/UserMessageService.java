package com.pilipala.service;

import com.pilipala.dto.UserMessageCountDTO;
import com.pilipala.entity.enums.MessageTypeEnum;
import com.pilipala.entity.query.UserMessageQuery;
import com.pilipala.entity.po.UserMessage;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 用户消息表对应的Service
 * @date: 2025/11/15
 */

public interface UserMessageService{

	/**
	 * 根据条件查询列表
	 */
	List<UserMessage>findListByParam(UserMessageQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(UserMessageQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserMessage> findListByPage(UserMessageQuery query );

	/**
	 * 新增
	 */
	Integer add(UserMessage bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserMessage> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(UserMessage bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserMessage> listBean);
	/**
	 * 根据MessageId查询
	 */
	 UserMessage getByMessageId(Integer messageId);

	/**
	 * 根据MessageId查询
	 */
	 Integer updateByMessageId(UserMessage bean , Integer messageId);

	/**
	 * 根据MessageId删除
	 */
	 Integer deleteByMessageId(Integer messageId);

	 void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageType, String content, Integer replyCommentId);

	 List<UserMessageCountDTO> getMessageTypeNoReadCount(String userId);

	Integer updateByParam(UserMessage userMessage, UserMessageQuery query);

	Integer deleteByParam(UserMessageQuery query);
}