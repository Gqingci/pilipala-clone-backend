package com.pilipala.mappers;

import com.pilipala.entity.query.UserVideoSeriesVideoQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author Gqingci
 * @Description: 的Mapper类
 * @date: 2025/10/24
 */

@Mapper
public interface UserVideoSeriesVideoMapper<T,P> extends BaseMapper {
	/**
	 * 根据SeriesIdAndVideoId查询
	 */
	 T selectBySeriesIdAndVideoId(@Param("seriesId") Integer seriesId,@Param("videoId") String videoId);

	/**
	 * 根据SeriesIdAndVideoId更新
	 */
	 Integer updateBySeriesIdAndVideoId(@Param("bean") T t, @Param("seriesId") Integer seriesId,@Param("videoId") String videoId);

	/**
	 * 根据SeriesIdAndVideoId删除
	 */
	 Integer deleteBySeriesIdAndVideoId(@Param("seriesId") Integer seriesId,@Param("videoId") String videoId);

	 Integer selectMaxSort(@Param("seriesId") Integer seriesId);

	int deleteByParam(@Param("query") UserVideoSeriesVideoQuery query);
}