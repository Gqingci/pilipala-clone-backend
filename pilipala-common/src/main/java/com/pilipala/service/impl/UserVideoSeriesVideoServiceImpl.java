package com.pilipala.service.impl;

import com.pilipala.entity.query.UserVideoSeriesVideoQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.UserVideoSeriesVideo;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.mappers.UserVideoSeriesVideoMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.UserVideoSeriesVideoService;
/**
 * @author Gqingci
 * @Description: 对应的ServiceImpl
 * @date: 2025/10/24
 */

@Service("userVideoSeriesVideoService")
public class UserVideoSeriesVideoServiceImpl implements UserVideoSeriesVideoService{

	@Resource
	private UserVideoSeriesVideoMapper<UserVideoSeriesVideo,UserVideoSeriesVideoQuery> userVideoSeriesVideoMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserVideoSeriesVideo>findListByParam(UserVideoSeriesVideoQuery query){
		return this.userVideoSeriesVideoMapper.selectList(query);
	 }
	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(UserVideoSeriesVideoQuery query){
		return this.userVideoSeriesVideoMapper.selectCount(query);
	 }
	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<UserVideoSeriesVideo> findListByPage(UserVideoSeriesVideoQuery query ){
		Integer count = this.findCountByParam(query); 
		Integer pageSize=query.getPageSize()==null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<UserVideoSeriesVideo> list = this.findListByParam(query);
		PaginationResultVO<UserVideoSeriesVideo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	 }
	/**
	 * 新增
	 */
	@Override
	public Integer add(UserVideoSeriesVideo bean){
		return this.userVideoSeriesVideoMapper.insert(bean);
	 }
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserVideoSeriesVideo> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.userVideoSeriesVideoMapper.insertBatch(listBean);
	 }
	/**
	 * 新增或者修改
	 */
	@Override
	public Integer addOrUpdate(UserVideoSeriesVideo userVideoSeriesVideo){
		return this.userVideoSeriesVideoMapper.insertOrUpdate(userVideoSeriesVideo);
	 }
	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserVideoSeriesVideo> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.userVideoSeriesVideoMapper.insertOrUpdateBatch(listBean);
	 }
	/**
	 * 根据SeriesIdAndVideoId查询
	 */
	@Override
	 public UserVideoSeriesVideo getBySeriesIdAndVideoId(Integer seriesId,String videoId){
		return this.userVideoSeriesVideoMapper.selectBySeriesIdAndVideoId(seriesId,videoId);
	 }
	/**
	 * 根据SeriesIdAndVideoId更新
	 */
	@Override
	 public Integer updateBySeriesIdAndVideoId(UserVideoSeriesVideo bean , Integer seriesId,String videoId){
		return this.userVideoSeriesVideoMapper.updateBySeriesIdAndVideoId(bean,seriesId,videoId);
	 }
	/**
	 * 根据SeriesIdAndVideoId删除
	 */
	@Override
	 public Integer deleteBySeriesIdAndVideoId(Integer seriesId,String videoId){
		return this.userVideoSeriesVideoMapper.deleteBySeriesIdAndVideoId(seriesId,videoId);
	 }

}