package com.pilipala.service;

import com.pilipala.entity.query.StatisticsQuery;
import com.pilipala.entity.po.Statistics;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
import java.util.Map;

/**
 * @author Gqingci
 * @Description: 数据统计对应的Service
 * @date: 2025/11/15
 */

public interface StatisticsService{

	/**
	 * 根据条件查询列表
	 */
	List<Statistics>findListByParam(StatisticsQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(StatisticsQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Statistics> findListByPage(StatisticsQuery query );

	/**
	 * 新增
	 */
	Integer add(Statistics bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<Statistics> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(Statistics bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<Statistics> listBean);
	/**
	 * 根据StatisticsDateAndUserIdAndDataType查询
	 */
	 Statistics getByStatisticsDateAndUserIdAndDataType(String statisticsDate,String userId,Integer dataType);

	/**
	 * 根据StatisticsDateAndUserIdAndDataType查询
	 */
	 Integer updateByStatisticsDateAndUserIdAndDataType(Statistics bean , String statisticsDate,String userId,Integer dataType);

	/**
	 * 根据StatisticsDateAndUserIdAndDataType删除
	 */
	 Integer deleteByStatisticsDateAndUserIdAndDataType(String statisticsDate,String userId,Integer dataType);

	void statisticsData();

	Map<String, Integer> getStatisticsActualTime(String userId);

	List< Statistics> findListTotalByParam(StatisticsQuery query);

	List<Statistics> findUserCountTotalByParam(StatisticsQuery param);
}