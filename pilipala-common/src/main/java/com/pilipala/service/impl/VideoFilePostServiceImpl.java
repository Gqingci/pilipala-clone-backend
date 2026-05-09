package com.pilipala.service.impl;

import com.pilipala.entity.query.VideoFilePostQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.mappers.VideoFilePostMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.VideoFilePostService;
/**
 * @author Gqingci
 * @Description: 视频文件信息对应的ServiceImpl
 * @date: 2025/10/02
 */

@Service("videoFilePostService")
public class VideoFilePostServiceImpl implements VideoFilePostService{

	@Resource
	private VideoFilePostMapper<VideoFilePost,VideoFilePostQuery> videoFilePostMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<VideoFilePost>findListByParam(VideoFilePostQuery query){
		return this.videoFilePostMapper.selectList(query);
	 }
	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(VideoFilePostQuery query){
		return this.videoFilePostMapper.selectCount(query);
	 }
	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<VideoFilePost> findListByPage(VideoFilePostQuery query ){
		Integer count = this.findCountByParam(query); 
		Integer pageSize=query.getPageSize()==null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<VideoFilePost> list = this.findListByParam(query);
		PaginationResultVO<VideoFilePost> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	 }
	/**
	 * 新增
	 */
	@Override
	public Integer add(VideoFilePost bean){
		return this.videoFilePostMapper.insert(bean);
	 }
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<VideoFilePost> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.videoFilePostMapper.insertBatch(listBean);
	 }
	/**
	 * 新增或者修改
	 */
	@Override
	public Integer addOrUpdate(VideoFilePost videoFilePost){
		return this.videoFilePostMapper.insertOrUpdate(videoFilePost);
	 }
	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoFilePost> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.videoFilePostMapper.insertOrUpdateBatch(listBean);
	 }
	/**
	 * 根据FileId查询
	 */
	@Override
	 public VideoFilePost getByFileId(String fileId){
		return this.videoFilePostMapper.selectByFileId(fileId);
	 }
	/**
	 * 根据FileId更新
	 */
	@Override
	 public Integer updateByFileId(VideoFilePost bean , String fileId){
		return this.videoFilePostMapper.updateByFileId(bean,fileId);
	 }
	/**
	 * 根据FileId删除
	 */
	@Override
	 public Integer deleteByFileId(String fileId){
		return this.videoFilePostMapper.deleteByFileId(fileId);
	 }
	/**
	 * 根据UploadIdAndUserId查询
	 */
	@Override
	 public VideoFilePost getByUploadIdAndUserId(String uploadId,String userId){
		return this.videoFilePostMapper.selectByUploadIdAndUserId(uploadId,userId);
	 }
	/**
	 * 根据UploadIdAndUserId更新
	 */
	@Override
	 public Integer updateByUploadIdAndUserId(VideoFilePost bean , String uploadId,String userId){
		return this.videoFilePostMapper.updateByUploadIdAndUserId(bean,uploadId,userId);
	 }
	/**
	 * 根据UploadIdAndUserId删除
	 */
	@Override
	 public Integer deleteByUploadIdAndUserId(String uploadId,String userId){
		return this.videoFilePostMapper.deleteByUploadIdAndUserId(uploadId,userId);
	 }

}