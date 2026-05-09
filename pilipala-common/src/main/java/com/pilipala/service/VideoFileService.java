package com.pilipala.service;

import com.pilipala.entity.query.VideoFileQuery;
import com.pilipala.entity.po.VideoFile;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 视频文件信息对应的Service
 * @date: 2025/10/02
 */

public interface VideoFileService{

	/**
	 * 根据条件查询列表
	 */
	List<VideoFile>findListByParam(VideoFileQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(VideoFileQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoFile> findListByPage(VideoFileQuery query );

	/**
	 * 新增
	 */
	Integer add(VideoFile bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoFile> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(VideoFile bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<VideoFile> listBean);
	/**
	 * 根据FileId查询
	 */
	 VideoFile getByFileId(String fileId);

	/**
	 * 根据FileId查询
	 */
	 Integer updateByFileId(VideoFile bean , String fileId);

	/**
	 * 根据FileId删除
	 */
	 Integer deleteByFileId(String fileId);


}