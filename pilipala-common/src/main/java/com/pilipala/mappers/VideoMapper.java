package com.pilipala.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author Gqingci
 * @Description: 视频信息的Mapper类
 * @date: 2025/10/02
 */

@Mapper
public interface VideoMapper<T,P> extends BaseMapper {
	/**
	 * 根据VideoId查询
	 */
	 T selectByVideoId(@Param("videoId") String videoId);

	/**
	 * 根据VideoId更新
	 */
	 Integer updateByVideoId(@Param("bean") T t, @Param("videoId") String videoId);

	/**
	 * 根据VideoId删除
	 */
	 Integer deleteByVideoId(@Param("videoId") String videoId);

}