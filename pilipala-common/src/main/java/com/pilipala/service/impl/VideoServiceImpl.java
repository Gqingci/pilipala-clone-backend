package com.pilipala.service.impl;

import com.pilipala.component.EsSearchComponent;
import com.pilipala.component.RedisComponent;
import com.pilipala.dto.SysSettingDTO;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.enums.VideoRecommendTypeEnum;
import com.pilipala.entity.po.*;
import com.pilipala.entity.query.*;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.*;
import com.pilipala.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;

import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.VideoService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gqingci
 * @Description: 视频信息对应的ServiceImpl
 * @date: 2025/10/02
 */

@Service("videoService")
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Resource
    private VideoMapper<Video, VideoQuery> videoMapper;

    @Resource
    private VideoFileMapper<VideoFile, VideoFileQuery> videoFileMapper;

    @Resource
    private VideoPostMapper<VideoPost, VideoPostQuery> videoPostMapper;

    @Resource
    private VideoDanmuMapper<VideoDanmu, VideoDanmuQuery> videoDanmuMapper;

    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

    @Resource
    private VideoFilePostMapper<VideoFilePost, VideoFilePostQuery> videoFilePostMapper;

    @Resource
    private EsSearchComponent esSearchComponent;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UsersMapper<Users, UsersQuery> usersMapper;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Video> findListByParam(VideoQuery query) {
        return this.videoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(VideoQuery query) {
        return this.videoMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<Video> findListByPage(VideoQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<Video> list = this.findListByParam(query);
        PaginationResultVO<Video> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Video bean) {
        return this.videoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Video> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(Video video) {
        return this.videoMapper.insertOrUpdate(video);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Video> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据VideoId查询
     */
    @Override
    public Video getByVideoId(String videoId) {
        return this.videoMapper.selectByVideoId(videoId);
    }

    /**
     * 根据VideoId更新
     */
    @Override
    public Integer updateByVideoId(Video bean, String videoId) {
        return this.videoMapper.updateByVideoId(bean, videoId);
    }

    /**
     * 根据VideoId删除
     */
    @Override
    public Integer deleteByVideoId(String videoId) {
        return this.videoMapper.deleteByVideoId(videoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeInteraction(String videoId, String userId, String interaction) {
        Video video = new Video();
        video.setInteraction(interaction);
        VideoQuery videoQuery = new VideoQuery();
        videoQuery.setVideoId(videoId);
        videoQuery.setUserId(userId);
        videoMapper.updateByParam(video, videoQuery);

        VideoPost videoPost = new VideoPost();
        videoPost.setInteraction(interaction);
        VideoPostQuery videoPostQuery = new VideoPostQuery();
        videoPostQuery.setVideoId(videoId);
        videoPostQuery.setUserId(userId);
        videoPostMapper.updateByParam(videoPost, videoPostQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVideo(String videoId, String userId) {
        VideoPost videoPost = this.videoPostMapper.selectByVideoId(videoId);
        if (videoPost == null || userId != null && !videoPost.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }

        this.videoMapper.deleteByVideoId(videoId);
        this.videoPostMapper.deleteByVideoId(videoId);

        // 减去用户硬币
        SysSettingDTO sysSettingDTO = redisComponent.getSysSettingDTO();
        usersMapper.updateCoinCount(videoPost.getUserId(), -sysSettingDTO.getPostVideoCoinCount());

        // 删除es索引
        esSearchComponent.delDoc(videoId);

        executorService.execute(() -> {
            VideoFileQuery videoFileQuery = new VideoFileQuery();
            videoFileQuery.setVideoId(videoId);

            // 删除分P
            videoFileMapper.deleteByParam(videoFileQuery);

            VideoFilePostQuery videoFilePostQuery = new VideoFilePostQuery();
            videoFilePostQuery.setVideoId(videoId);
            videoFilePostMapper.deleteByParam(videoFilePostQuery);

            // 删除弹幕
            VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
            videoDanmuQuery.setVideoId(videoId);
            videoDanmuMapper.deleteByParam(videoDanmuQuery);

            // 删除评论
            VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
            videoCommentQuery.setVideoId(videoId);
            videoCommentMapper.deleteByParam(videoCommentQuery);

            // 删除视频封面
            VideoQuery videoQuery = new VideoQuery();
            videoQuery.setVideoId(videoId);
            List<Video> videoList = this.videoMapper.selectList(videoQuery);
            for (Video video : videoList) {
                try {
                    String videoCoverPath = video.getVideoCover().split("/")[1];
                    FileUtils.deleteDirectory(new File(Constants.FILE_FOLDER + Constants.FILE_COVER + videoCoverPath));
                } catch (IOException e) {
                    log.error("删除视频封面失败,文件路径：{}", video.getVideoCover());
                }
            }

            // 删除视频文件
            List<VideoFile> videoFileList = this.videoFileMapper.selectList(videoFileQuery);
            for (VideoFile videoFile : videoFileList) {
                try {
                    FileUtils.deleteDirectory(new File(Constants.FILE_FOLDER + videoFile.getFilePath()));
                } catch (IOException e) {
                    log.error("删除视频文件失败,文件路径：{}", videoFile.getFilePath());
                }
            }
        });
    }

    @Override
    public void addReadCount(String videoId) {
        this.videoMapper.updateCount(videoId, UserActionTypeEnum.VIDEO_PLAY.getField(), 1);
    }

    @Override
    public void recommendVideo(String videoId) {
        Video video = this.videoMapper.selectByVideoId(videoId);
        if (video == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        Integer recommendType;
        if (VideoRecommendTypeEnum.RECOMMEND.getType().equals(video.getRecommendType())) {
            recommendType = VideoRecommendTypeEnum.NO_RECOMMEND.getType();
        } else {
            recommendType = VideoRecommendTypeEnum.RECOMMEND.getType();
        }
        Video updateVideo = new Video();
        updateVideo.setRecommendType(recommendType);
        this.videoMapper.updateByVideoId(updateVideo, videoId);
    }
}