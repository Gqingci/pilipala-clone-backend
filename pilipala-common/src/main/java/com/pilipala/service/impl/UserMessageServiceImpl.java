package com.pilipala.service.impl;

import com.pilipala.dto.UserMessageCountDTO;
import com.pilipala.dto.UserMessageExtendDTO;
import com.pilipala.entity.enums.MessageReadTypeEnum;
import com.pilipala.entity.enums.MessageTypeEnum;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.po.VideoComment;
import com.pilipala.entity.po.VideoPost;
import com.pilipala.entity.query.*;
import com.pilipala.entity.po.UserMessage;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.mappers.UserMessageMapper;
import com.pilipala.mappers.VideoCommentMapper;
import com.pilipala.mappers.VideoMapper;
import com.pilipala.mappers.VideoPostMapper;
import com.pilipala.utils.JsonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.UserMessageService;

/**
 * @author Gqingci
 * @Description: 用户消息表对应的ServiceImpl
 * @date: 2025/11/15
 */

@Service("userMessageService")
public class UserMessageServiceImpl implements UserMessageService {

    @Resource
    private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

    @Resource
    private VideoMapper<Video, VideoQuery> videoMapper;

    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

    @Resource
    private VideoPostMapper<VideoPost, VideoPostQuery> videoPostMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<UserMessage> findListByParam(UserMessageQuery query) {
        return this.userMessageMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(UserMessageQuery query) {
        return this.userMessageMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<UserMessage> findListByPage(UserMessageQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserMessage> list = this.findListByParam(query);
        PaginationResultVO<UserMessage> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(UserMessage bean) {
        return this.userMessageMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<UserMessage> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userMessageMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(UserMessage userMessage) {
        return this.userMessageMapper.insertOrUpdate(userMessage);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<UserMessage> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userMessageMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据MessageId查询
     */
    @Override
    public UserMessage getByMessageId(Integer messageId) {
        return this.userMessageMapper.selectByMessageId(messageId);
    }

    /**
     * 根据MessageId更新
     */
    @Override
    public Integer updateByMessageId(UserMessage bean, Integer messageId) {
        return this.userMessageMapper.updateByMessageId(bean, messageId);
    }

    /**
     * 根据MessageId删除
     */
    @Override
    public Integer deleteByMessageId(Integer messageId) {
        return this.userMessageMapper.deleteByMessageId(messageId);
    }

    @Override
    public Integer updateByParam(UserMessage userMessage, UserMessageQuery query) {
        return this.userMessageMapper.updateByParam(userMessage, query);
    }

    @Override
    public Integer deleteByParam(UserMessageQuery query) {
        return this.userMessageMapper.deleteByParam(query);
    }

    @Override
    @Async
    public void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageType, String content, Integer replyCommentId) {
        Video video = videoMapper.selectByVideoId(videoId);
        if (video == null) {
            return;
        }

        UserMessageExtendDTO userMessageExtendDTO = new UserMessageExtendDTO();
        userMessageExtendDTO.setMessageContent(content);

        String userId = video.getUserId();

        // 收藏，点赞，已经记录了的不再记录。
        if (ArrayUtils.contains(new Integer[]{MessageTypeEnum.COLLECTION.getType(), MessageTypeEnum.LIKE.getType()}, messageType.getType())) {
            UserMessageQuery query = new UserMessageQuery();
            query.setUserId(userId);
            query.setVideoId(videoId);
            query.setMessageType(messageType.getType());
            if (this.findCountByParam(query) > 0) {
                return;
            }
        }

        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(userId);
        userMessage.setVideoId(videoId);
        userMessage.setMessageType(messageType.getType());
        userMessage.setSendUserId(sendUserId);
        userMessage.setReadType(MessageReadTypeEnum.NO_READ.getType());
        userMessage.setCreateTime(new Date());
        // 特殊评论处理
        if(replyCommentId != null) {
            VideoComment videoComment = videoCommentMapper.selectByCommentId(replyCommentId);
            if(videoComment != null) {
                userId = videoComment.getUserId();
                userMessageExtendDTO.setMessageContentReply(videoComment.getContent());
            }
        }
        if(userId.equals(sendUserId)) {
            return;
        }

        // 系统消息特殊处理
        if(messageType == MessageTypeEnum.SYS) {
            VideoPost videoPost = videoPostMapper.selectByVideoId(videoId);
            userMessageExtendDTO.setAuditStatus(videoPost.getStatus());
        }

        userMessage.setUserId(userId);
        userMessage.setExtendJson(JsonUtils.convertObj2Json(userMessageExtendDTO));
        this.userMessageMapper.insert(userMessage);
    }

    @Override
    public List<UserMessageCountDTO> getMessageTypeNoReadCount(String userId) {
        return this.userMessageMapper.getMessageTypeNoReadCount(userId);
    }


}