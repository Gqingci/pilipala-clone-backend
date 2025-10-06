package com.pilipala.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author Gqingci
 * @Description: 视频弹幕的Mapper类
 * @date: 2025/10/05
 */

@Mapper
public interface VideoDanmuMapper<T,P> extends BaseMapper {
	/**
	 * 根据DanmuId查询
	 */
	 T selectByDanmuId(@Param("danmuId") Integer danmuId);

	/**
	 * 根据DanmuId更新
	 */
	 Integer updateByDanmuId(@Param("bean") T t, @Param("danmuId") Integer danmuId);

	/**
	 * 根据DanmuId删除
	 */
	 Integer deleteByDanmuId(@Param("danmuId") Integer danmuId);

}