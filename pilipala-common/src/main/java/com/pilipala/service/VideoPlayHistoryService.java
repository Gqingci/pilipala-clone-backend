package com.pilipala.service;

import com.pilipala.entity.query.VideoPlayHistoryQuery;
import com.pilipala.entity.po.VideoPlayHistory;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 视频播放历史对应的Service
 * @date: 2025/11/15
 */

public interface VideoPlayHistoryService{

	/**
	 * 根据条件查询列表
	 */
	List<VideoPlayHistory>findListByParam(VideoPlayHistoryQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(VideoPlayHistoryQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoPlayHistory> findListByPage(VideoPlayHistoryQuery query );

	/**
	 * 新增
	 */
	Integer add(VideoPlayHistory bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoPlayHistory> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(VideoPlayHistory bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<VideoPlayHistory> listBean);
	/**
	 * 根据UserIdAndVideoId查询
	 */
	 VideoPlayHistory getByUserIdAndVideoId(String userId,String videoId);

	/**
	 * 根据UserIdAndVideoId查询
	 */
	 Integer updateByUserIdAndVideoId(VideoPlayHistory bean , String userId,String videoId);

	/**
	 * 根据UserIdAndVideoId删除
	 */
	 Integer deleteByUserIdAndVideoId(String userId,String videoId);

	void saveHistory(String userId, String videoId, Integer fileIndex);

    Integer deleteByParam(VideoPlayHistoryQuery query);
}