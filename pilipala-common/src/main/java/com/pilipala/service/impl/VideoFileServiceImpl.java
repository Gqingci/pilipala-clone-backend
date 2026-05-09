package com.pilipala.service.impl;

import com.pilipala.entity.query.VideoFileQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.VideoFile;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.mappers.VideoFileMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.VideoFileService;
/**
 * @author Gqingci
 * @Description: 视频文件信息对应的ServiceImpl
 * @date: 2025/10/02
 */

@Service("videoFileService")
public class VideoFileServiceImpl implements VideoFileService{

	@Resource
	private VideoFileMapper<VideoFile,VideoFileQuery> videoFileMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<VideoFile>findListByParam(VideoFileQuery query){
		return this.videoFileMapper.selectList(query);
	 }
	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(VideoFileQuery query){
		return this.videoFileMapper.selectCount(query);
	 }
	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<VideoFile> findListByPage(VideoFileQuery query ){
		Integer count = this.findCountByParam(query); 
		Integer pageSize=query.getPageSize()==null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<VideoFile> list = this.findListByParam(query);
		PaginationResultVO<VideoFile> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	 }
	/**
	 * 新增
	 */
	@Override
	public Integer add(VideoFile bean){
		return this.videoFileMapper.insert(bean);
	 }
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<VideoFile> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.videoFileMapper.insertBatch(listBean);
	 }
	/**
	 * 新增或者修改
	 */
	@Override
	public Integer addOrUpdate(VideoFile videoFile){
		return this.videoFileMapper.insertOrUpdate(videoFile);
	 }
	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoFile> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.videoFileMapper.insertOrUpdateBatch(listBean);
	 }
	/**
	 * 根据FileId查询
	 */
	@Override
	 public VideoFile getByFileId(String fileId){
		return this.videoFileMapper.selectByFileId(fileId);
	 }
	/**
	 * 根据FileId更新
	 */
	@Override
	 public Integer updateByFileId(VideoFile bean , String fileId){
		return this.videoFileMapper.updateByFileId(bean,fileId);
	 }
	/**
	 * 根据FileId删除
	 */
	@Override
	 public Integer deleteByFileId(String fileId){
		return this.videoFileMapper.deleteByFileId(fileId);
	 }

}