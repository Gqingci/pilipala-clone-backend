package com.pilipala.service;

import com.pilipala.entity.query.UserVideoSeriesVideoQuery;
import com.pilipala.entity.po.UserVideoSeriesVideo;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 对应的Service
 * @date: 2025/10/24
 */

public interface UserVideoSeriesVideoService{

	/**
	 * 根据条件查询列表
	 */
	List<UserVideoSeriesVideo>findListByParam(UserVideoSeriesVideoQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(UserVideoSeriesVideoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserVideoSeriesVideo> findListByPage(UserVideoSeriesVideoQuery query );

	/**
	 * 新增
	 */
	Integer add(UserVideoSeriesVideo bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserVideoSeriesVideo> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(UserVideoSeriesVideo bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserVideoSeriesVideo> listBean);
	/**
	 * 根据SeriesIdAndVideoId查询
	 */
	 UserVideoSeriesVideo getBySeriesIdAndVideoId(Integer seriesId,String videoId);

	/**
	 * 根据SeriesIdAndVideoId查询
	 */
	 Integer updateBySeriesIdAndVideoId(UserVideoSeriesVideo bean , Integer seriesId,String videoId);

	/**
	 * 根据SeriesIdAndVideoId删除
	 */
	 Integer deleteBySeriesIdAndVideoId(Integer seriesId,String videoId);


}