package com.pilipala.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author Gqingci
 * @Description: 视频播放历史的Mapper类
 * @date: 2025/11/15
 */

@Mapper
public interface VideoPlayHistoryMapper<T,P> extends BaseMapper {
	/**
	 * 根据UserIdAndVideoId查询
	 */
	 T selectByUserIdAndVideoId(@Param("userId") String userId,@Param("videoId") String videoId);

	/**
	 * 根据UserIdAndVideoId更新
	 */
	 Integer updateByUserIdAndVideoId(@Param("bean") T t, @Param("userId") String userId,@Param("videoId") String videoId);

	/**
	 * 根据UserIdAndVideoId删除
	 */
	 Integer deleteByUserIdAndVideoId(@Param("userId") String userId,@Param("videoId") String videoId);

    Integer deleteByParam(@Param("query") P query);
}