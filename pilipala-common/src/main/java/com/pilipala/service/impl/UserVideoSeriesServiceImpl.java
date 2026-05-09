package com.pilipala.service.impl;

import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.po.UserVideoSeriesVideo;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.query.UserVideoSeriesQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.UserVideoSeries;
import com.pilipala.entity.query.UserVideoSeriesVideoQuery;
import com.pilipala.entity.query.VideoQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.UserVideoSeriesMapper;
import com.pilipala.mappers.UserVideoSeriesVideoMapper;
import com.pilipala.mappers.VideoMapper;
import com.pilipala.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.UserVideoSeriesService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gqingci
 * @Description: 用户视频序列归档对应的ServiceImpl
 * @date: 2025/10/24
 */

@Service("userVideoSeriesService")
public class UserVideoSeriesServiceImpl implements UserVideoSeriesService{

	@Resource
	private UserVideoSeriesMapper<UserVideoSeries,UserVideoSeriesQuery> userVideoSeriesMapper;

	@Resource
	private VideoMapper<Video, VideoQuery> videoMapper;

	@Resource
	private UserVideoSeriesVideoMapper<UserVideoSeriesVideo, UserVideoSeriesVideoQuery> userVideoSeriesVideoMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserVideoSeries>findListByParam(UserVideoSeriesQuery query){
		return this.userVideoSeriesMapper.selectList(query);
	 }
	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(UserVideoSeriesQuery query){
		return this.userVideoSeriesMapper.selectCount(query);
	 }
	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<UserVideoSeries> findListByPage(UserVideoSeriesQuery query ){
		Integer count = this.findCountByParam(query); 
		Integer pageSize=query.getPageSize()==null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<UserVideoSeries> list = this.findListByParam(query);
		PaginationResultVO<UserVideoSeries> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	 }
	/**
	 * 新增
	 */
	@Override
	public Integer add(UserVideoSeries bean){
		return this.userVideoSeriesMapper.insert(bean);
	 }
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserVideoSeries> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.userVideoSeriesMapper.insertBatch(listBean);
	 }
	/**
	 * 新增或者修改
	 */
	@Override
	public Integer addOrUpdate(UserVideoSeries userVideoSeries){
		return this.userVideoSeriesMapper.insertOrUpdate(userVideoSeries);
	 }
	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserVideoSeries> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.userVideoSeriesMapper.insertOrUpdateBatch(listBean);
	 }
	/**
	 * 根据SeriesId查询
	 */
	@Override
	 public UserVideoSeries getBySeriesId(Integer seriesId){
		return this.userVideoSeriesMapper.selectBySeriesId(seriesId);
	 }
	/**
	 * 根据SeriesId更新
	 */
	@Override
	 public Integer updateBySeriesId(UserVideoSeries bean , Integer seriesId){
		return this.userVideoSeriesMapper.updateBySeriesId(bean,seriesId);
	 }
	/**
	 * 根据SeriesId删除
	 */
	@Override
	 public Integer deleteBySeriesId(Integer seriesId){
		return this.userVideoSeriesMapper.deleteBySeriesId(seriesId);
	 }

    @Override
    public List<UserVideoSeries> getUserAllSeries(String userId) {
        return userVideoSeriesMapper.selectUserAllSeries(userId);
    }

	@Override
	@Transactional
	public void saveUserVideoSeries(UserVideoSeries userVideoSeries, String videoIds) {
		if(userVideoSeries.getSeriesId() == null && StringUtils.isEmpty(videoIds)) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		if(userVideoSeries.getSeriesId() == null) {
			checkVideoIds(userVideoSeries.getUserId(), videoIds);

			userVideoSeries.setUpdateTime(new Date());
			userVideoSeries.setSort(this.userVideoSeriesMapper.selectMaxSort(userVideoSeries.getUserId()) + 1);
			this.userVideoSeriesMapper.insert(userVideoSeries);

			this.saveSeriesVideo(userVideoSeries.getUserId(), userVideoSeries.getSeriesId(), videoIds);
		} else {
			UserVideoSeriesQuery query = new UserVideoSeriesQuery();
			query.setUserId(userVideoSeries.getUserId());
			query.setSeriesId(userVideoSeries.getSeriesId());
			this.userVideoSeriesMapper.updateByParam(userVideoSeries, query);
		}
	}

	@Override
	public void saveSeriesVideo(String userId, Integer seriesId, String videoIds) {
		UserVideoSeries userVideoSeries = getBySeriesId(seriesId);
		if(userVideoSeries == null || !userVideoSeries.getUserId().equals(userId)) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		this.checkVideoIds(userId, videoIds);

		String[] videoIdArray = videoIds.split(",");
		Integer sort = this.userVideoSeriesVideoMapper.selectMaxSort(seriesId);

		List<UserVideoSeriesVideo> seriesVideoList = new ArrayList<>();
		for (String videoId : videoIdArray) {
			UserVideoSeriesVideo videoSeriesVideo = new UserVideoSeriesVideo();
			videoSeriesVideo.setSeriesId(seriesId);
			videoSeriesVideo.setSort(++sort);
			videoSeriesVideo.setVideoId(videoId);
			videoSeriesVideo.setUserId(userId);
			seriesVideoList.add(videoSeriesVideo);
		}
		this.userVideoSeriesVideoMapper.insertOrUpdateBatch(seriesVideoList);
	}

	@Override
	public void delSeriesVideo(String userId, Integer seriesId, String videoId) {
		UserVideoSeriesVideoQuery query = new UserVideoSeriesVideoQuery();
		query.setUserId(userId);
		query.setSeriesId(seriesId);
		query.setVideoId(videoId);
		this.userVideoSeriesVideoMapper.deleteByParam(query);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delVideoSeries(String userId, Integer seriesId) {
		UserVideoSeriesQuery query = new UserVideoSeriesQuery();
		query.setUserId(userId);
		query.setSeriesId(seriesId);
		Integer count = this.userVideoSeriesMapper.deleteByParam(query);
		if(count == 0) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		UserVideoSeriesVideoQuery videoQuery = new UserVideoSeriesVideoQuery();
		videoQuery.setUserId(userId);
		videoQuery.setSeriesId(seriesId);
		this.userVideoSeriesVideoMapper.deleteByParam(videoQuery);
	}

	@Override
	public void changeVideoSeriesSort(String userId, String seriesIds) {
		String[] seriesIdArray = seriesIds.split(",");
		List<UserVideoSeries> videoSeriesList = new ArrayList<>();
		Integer sort = 0;
		for (String seriesId : seriesIdArray) {
			UserVideoSeries userVideoSeries = new UserVideoSeries();
			userVideoSeries.setUserId(userId);
			userVideoSeries.setSeriesId(Integer.parseInt(seriesId));
			userVideoSeries.setSort(++sort);
			videoSeriesList.add(userVideoSeries);
		}
		userVideoSeriesMapper.changeSort(videoSeriesList);
	}

	@Override
	public List<UserVideoSeries> findListWithVideoList(UserVideoSeriesQuery seriesQuery) {
		return userVideoSeriesMapper.selectListWithVideo(seriesQuery);
	}

	private void checkVideoIds(String userId, String videoIds) {
		String[] videoIdArray = videoIds.split(",");
		VideoQuery videoQuery = new VideoQuery();
		videoQuery.setUserId(userId);
		videoQuery.setVideoIdArray(videoIdArray);
		Integer count = videoMapper.selectCount(videoQuery);
		if(videoIdArray.length != count) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
	}
}