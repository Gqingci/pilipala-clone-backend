package com.pilipala.service;

import com.pilipala.entity.query.VideoFilePostQuery;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 视频文件信息对应的Service
 * @date: 2025/10/02
 */

public interface VideoFilePostService{

	/**
	 * 根据条件查询列表
	 */
	List<VideoFilePost>findListByParam(VideoFilePostQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(VideoFilePostQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoFilePost> findListByPage(VideoFilePostQuery query );

	/**
	 * 新增
	 */
	Integer add(VideoFilePost bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoFilePost> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(VideoFilePost bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<VideoFilePost> listBean);
	/**
	 * 根据FileId查询
	 */
	 VideoFilePost getByFileId(String fileId);

	/**
	 * 根据FileId查询
	 */
	 Integer updateByFileId(VideoFilePost bean , String fileId);

	/**
	 * 根据FileId删除
	 */
	 Integer deleteByFileId(String fileId);

	/**
	 * 根据UploadIdAndUserId查询
	 */
	 VideoFilePost getByUploadIdAndUserId(String uploadId,String userId);

	/**
	 * 根据UploadIdAndUserId查询
	 */
	 Integer updateByUploadIdAndUserId(VideoFilePost bean , String uploadId,String userId);

	/**
	 * 根据UploadIdAndUserId删除
	 */
	 Integer deleteByUploadIdAndUserId(String uploadId,String userId);


}