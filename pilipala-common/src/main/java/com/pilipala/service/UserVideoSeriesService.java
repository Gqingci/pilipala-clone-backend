package com.pilipala.service;

import com.pilipala.entity.query.UserVideoSeriesQuery;
import com.pilipala.entity.po.UserVideoSeries;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 用户视频序列归档对应的Service
 * @date: 2025/10/24
 */

public interface UserVideoSeriesService{

	/**
	 * 根据条件查询列表
	 */
	List<UserVideoSeries>findListByParam(UserVideoSeriesQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(UserVideoSeriesQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserVideoSeries> findListByPage(UserVideoSeriesQuery query );

	/**
	 * 新增
	 */
	Integer add(UserVideoSeries bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserVideoSeries> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(UserVideoSeries bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserVideoSeries> listBean);
	/**
	 * 根据SeriesId查询
	 */
	 UserVideoSeries getBySeriesId(Integer seriesId);

	/**
	 * 根据SeriesId查询
	 */
	 Integer updateBySeriesId(UserVideoSeries bean , Integer seriesId);

	/**
	 * 根据SeriesId删除
	 */
	 Integer deleteBySeriesId(Integer seriesId);


    List<UserVideoSeries> getUserAllSeries(String userId);

	void saveUserVideoSeries(UserVideoSeries userVideoSeries, String videoIds);

	void saveSeriesVideo(String userId, Integer seriesId, String videoIds);

    void delSeriesVideo(String userId, Integer seriesId, String videoId);

    void delVideoSeries(String userId, Integer seriesId);

	void changeVideoSeriesSort(String userId, String seriesIds);

	List<UserVideoSeries> findListWithVideoList(UserVideoSeriesQuery seriesQuery);
}