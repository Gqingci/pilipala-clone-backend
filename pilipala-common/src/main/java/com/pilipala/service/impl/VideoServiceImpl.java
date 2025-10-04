package com.pilipala.service.impl;

import com.pilipala.entity.query.VideoQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.mappers.VideoMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.VideoService;
/**
 * @author Gqingci
 * @Description: 视频信息对应的ServiceImpl
 * @date: 2025/10/02
 */

@Service("videoService")
public class VideoServiceImpl implements VideoService{

	@Resource
	private VideoMapper<Video,VideoQuery> videoMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Video>findListByParam(VideoQuery query){
		return this.videoMapper.selectList(query);
	 }
	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(VideoQuery query){
		return this.videoMapper.selectCount(query);
	 }
	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Video> findListByPage(VideoQuery query ){
		Integer count = this.findCountByParam(query); 
		Integer pageSize=query.getPageSize()==null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<Video> list = this.findListByParam(query);
		PaginationResultVO<Video> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	 }
	/**
	 * 新增
	 */
	@Override
	public Integer add(Video bean){
		return this.videoMapper.insert(bean);
	 }
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Video> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.videoMapper.insertBatch(listBean);
	 }
	/**
	 * 新增或者修改
	 */
	@Override
	public Integer addOrUpdate(Video video){
		return this.videoMapper.insertOrUpdate(video);
	 }
	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Video> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.videoMapper.insertOrUpdateBatch(listBean);
	 }
	/**
	 * 根据VideoId查询
	 */
	@Override
	 public Video getByVideoId(String videoId){
		return this.videoMapper.selectByVideoId(videoId);
	 }
	/**
	 * 根据VideoId更新
	 */
	@Override
	 public Integer updateByVideoId(Video bean , String videoId){
		return this.videoMapper.updateByVideoId(bean,videoId);
	 }
	/**
	 * 根据VideoId删除
	 */
	@Override
	 public Integer deleteByVideoId(String videoId){
		return this.videoMapper.deleteByVideoId(videoId);
	 }

}