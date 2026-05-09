package com.pilipala.mappers;

import com.pilipala.dto.UserMessageCountDTO;
import com.pilipala.entity.po.UserMessage;
import com.pilipala.entity.query.UserMessageQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Gqingci
 * @Description: 用户消息表的Mapper类
 * @date: 2025/11/15
 */

@Mapper
public interface UserMessageMapper<T, P> extends BaseMapper {
    /**
     * 根据MessageId查询
     */
    T selectByMessageId(@Param("messageId") Integer messageId);

    /**
     * 根据MessageId更新
     */
    Integer updateByMessageId(@Param("bean") T t, @Param("messageId") Integer messageId);

    /**
     * 根据MessageId删除
     */
    Integer deleteByMessageId(@Param("messageId") Integer messageId);

    List<UserMessageCountDTO> getMessageTypeNoReadCount(@Param("userId") String userId);

    Integer updateByParam(@Param("bean") UserMessage bean, @Param("query") UserMessageQuery query);

    Integer deleteByParam(@Param("query") UserMessageQuery query);
}