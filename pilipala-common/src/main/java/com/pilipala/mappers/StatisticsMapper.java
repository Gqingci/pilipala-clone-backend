package com.pilipala.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Gqingci
 * @Description: 数据统计的Mapper类
 * @date: 2025/11/15
 */

@Mapper
public interface StatisticsMapper<T, P> extends BaseMapper {
    /**
     * 根据StatisticsDateAndUserIdAndDataType查询
     */
    T selectByStatisticsDateAndUserIdAndDataType(@Param("statisticsDate") String statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);

    /**
     * 根据StatisticsDateAndUserIdAndDataType更新
     */
    Integer updateByStatisticsDateAndUserIdAndDataType(@Param("bean") T t, @Param("statisticsDate") String statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);

    /**
     * 根据StatisticsDateAndUserIdAndDataType删除
     */
    Integer deleteByStatisticsDateAndUserIdAndDataType(@Param("statisticsDate") String statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);

    List<T> selectStatisticsFans(@Param("statisticsDate") String statisticsDate);

    List<T> selectStatisticsComment(@Param("statisticsDate") String statisticsDate);

    List<T> selectStatisticsOther(@Param("statisticsDate") String statisticsDate, @Param("actionTypeArray") Integer[] actionTypeArray);

    List<T> selectStatisticsDanmu(@Param("statisticsDate") String statisticsDate);

    Map<String, Integer> selectTotalCount(@Param("userId") String userId);

    List<T> selectListTotalByParam(@Param("query") P query);

    List<T> selectUserCountTotalByParam(@Param("query") P param);
}