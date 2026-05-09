package com.pilipala.mappers;

import com.pilipala.entity.po.UserVideoSeries;
import com.pilipala.entity.query.UserVideoSeriesQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Gqingci
 * @Description: 用户视频序列归档的Mapper类
 * @date: 2025/10/24
 */

@Mapper
public interface UserVideoSeriesMapper<T, P> extends BaseMapper {
    /**
     * 根据SeriesId查询
     */
    T selectBySeriesId(@Param("seriesId") Integer seriesId);

    /**
     * 根据SeriesId更新
     */
    Integer updateBySeriesId(@Param("bean") T t, @Param("seriesId") Integer seriesId);

    /**
     * 根据SeriesId删除
     */
    Integer deleteBySeriesId(@Param("seriesId") Integer seriesId);

    List<T> selectUserAllSeries(@Param("userId") String userId);

    Integer selectMaxSort(@Param("userId") String userId);

    int updateByParam(@Param("bean") UserVideoSeries bean, @Param("query") UserVideoSeriesQuery query);

    int deleteByParam(@Param("query") UserVideoSeriesQuery query);

    void changeSort(@Param("videoSeriesList") List<UserVideoSeries> videoSeriesList);

    List<T> selectListWithVideo(@Param("query") P seriesQuery);
}