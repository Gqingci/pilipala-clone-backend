package com.pilipala.service;

import com.pilipala.entity.query.VideoQuery;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 视频信息对应的Service
 * @date: 2025/10/02
 */

public interface VideoService{

	/**
	 * 根据条件查询列表
	 */
	List<Video>findListByParam(VideoQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(VideoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Video> findListByPage(VideoQuery query );

	/**
	 * 新增
	 */
	Integer add(Video bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<Video> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(Video bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<Video> listBean);
	/**
	 * 根据VideoId查询
	 */
	 Video getByVideoId(String videoId);

	/**
	 * 根据VideoId查询
	 */
	 Integer updateByVideoId(Video bean , String videoId);

	/**
	 * 根据VideoId删除
	 */
	 Integer deleteByVideoId(String videoId);


}