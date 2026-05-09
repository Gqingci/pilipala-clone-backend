package com.pilipala.service.impl;

import com.pilipala.entity.query.VideoPlayHistoryQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.VideoPlayHistory;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.mappers.VideoPlayHistoryMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.VideoPlayHistoryService;
/**
 * @author Gqingci
 * @Description: 视频播放历史对应的ServiceImpl
 * @date: 2025/11/15
 */

@Service("videoPlayHistoryService")
public class VideoPlayHistoryServiceImpl implements VideoPlayHistoryService{

	@Resource
	private VideoPlayHistoryMapper<VideoPlayHistory,VideoPlayHistoryQuery> videoPlayHistoryMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<VideoPlayHistory>findListByParam(VideoPlayHistoryQuery query){
		return this.videoPlayHistoryMapper.selectList(query);
	 }
	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(VideoPlayHistoryQuery query){
		return this.videoPlayHistoryMapper.selectCount(query);
	 }
	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<VideoPlayHistory> findListByPage(VideoPlayHistoryQuery query ){
		Integer count = this.findCountByParam(query); 
		Integer pageSize=query.getPageSize()==null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<VideoPlayHistory> list = this.findListByParam(query);
		PaginationResultVO<VideoPlayHistory> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	 }
	/**
	 * 新增
	 */
	@Override
	public Integer add(VideoPlayHistory bean){
		return this.videoPlayHistoryMapper.insert(bean);
	 }
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<VideoPlayHistory> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.videoPlayHistoryMapper.insertBatch(listBean);
	 }
	/**
	 * 新增或者修改
	 */
	@Override
	public Integer addOrUpdate(VideoPlayHistory videoPlayHistory){
		return this.videoPlayHistoryMapper.insertOrUpdate(videoPlayHistory);
	 }
	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoPlayHistory> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.videoPlayHistoryMapper.insertOrUpdateBatch(listBean);
	 }
	/**
	 * 根据UserIdAndVideoId查询
	 */
	@Override
	 public VideoPlayHistory getByUserIdAndVideoId(String userId,String videoId){
		return this.videoPlayHistoryMapper.selectByUserIdAndVideoId(userId,videoId);
	 }
	/**
	 * 根据UserIdAndVideoId更新
	 */
	@Override
	 public Integer updateByUserIdAndVideoId(VideoPlayHistory bean , String userId,String videoId){
		return this.videoPlayHistoryMapper.updateByUserIdAndVideoId(bean,userId,videoId);
	 }
	/**
	 * 根据UserIdAndVideoId删除
	 */
	@Override
	 public Integer deleteByUserIdAndVideoId(String userId,String videoId){
		return this.videoPlayHistoryMapper.deleteByUserIdAndVideoId(userId,videoId);
	 }

	@Override
	public Integer deleteByParam(VideoPlayHistoryQuery query) {
		return this.videoPlayHistoryMapper.deleteByParam(query);
	}

    @Override
    public void saveHistory(String userId, String videoId, Integer fileIndex) {
        VideoPlayHistory videoPlayHistory = new VideoPlayHistory();
		videoPlayHistory.setUserId(userId);
		videoPlayHistory.setVideoId(videoId);
		videoPlayHistory.setFileIndex(fileIndex);
		videoPlayHistory.setLastUpdateTime(new java.util.Date());
		this.videoPlayHistoryMapper.insertOrUpdate(videoPlayHistory);
    }


}