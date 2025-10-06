package com.pilipala.web.controller;

import com.pilipala.component.RedisComponent;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.enums.VideoRecommendTypeEnum;
import com.pilipala.entity.po.UserAction;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.po.VideoFile;
import com.pilipala.entity.query.UserActionQuery;
import com.pilipala.entity.query.VideoFileQuery;
import com.pilipala.entity.query.VideoQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.entity.vo.VideoInfoResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.service.UserActionService;
import com.pilipala.service.VideoFileService;
import com.pilipala.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Video")
@Slf4j
public class VideoController extends ABaseController {
    @Resource
    private VideoService videoService;

    @Resource
    private VideoFileService videoFileService;

    @Resource
    private UserActionService userActionService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/loadRecommendVideo")
    public ResponseVO loadRecommendVideo() {
        VideoQuery videoQuery = new VideoQuery();
        videoQuery.setQueryUserInfo(true);
        videoQuery.setOrderBy("create_time desc");
        videoQuery.setRecommendType(VideoRecommendTypeEnum.RECOMMEND.getType());
        List<Video> recommendVideoList = videoService.findListByParam(videoQuery);

        return getSuccessResponseVo(recommendVideoList);
    }

    @RequestMapping("/loadVideo")
    public ResponseVO loadVideo(Integer pCategoryId, Integer categoryId, Integer pageNo) {
        VideoQuery videoQuery = new VideoQuery();
        videoQuery.setPCategoryId(pCategoryId);
        videoQuery.setCategoryId(categoryId);
        videoQuery.setPageNo(pageNo);
        videoQuery.setQueryUserInfo(true);
        videoQuery.setOrderBy("create_time desc");
        videoQuery.setRecommendType(VideoRecommendTypeEnum.NO_RECOMMEND.getType());
        PaginationResultVO<Video> paginationResultVO = videoService.findListByPage(videoQuery);

        return getSuccessResponseVo(paginationResultVO);
    }

    @RequestMapping("/getVideoInfo")
    public ResponseVO getVideoInfo(@NotEmpty String videoId) {
        Video video = videoService.getByVideoId(videoId);
        if (video == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }

        List<UserAction> userActionList = new ArrayList<>();
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        if (userTokenInfoDTO != null) {
            UserActionQuery userActionQuery = new UserActionQuery();
            userActionQuery.setVideoId(videoId);
            userActionQuery.setUserId(userTokenInfoDTO.getId());
            userActionQuery.setActionTypeArray(new Integer[]{
                    UserActionTypeEnum.VIDEO_LIKE.getType(),
                    UserActionTypeEnum.VIDEO_COLLECT.getType(),
                    UserActionTypeEnum.VIDEO_COIN.getType()});
            userActionList = userActionService.findListByParam(userActionQuery);
        }

        VideoInfoResultVO resultVO = new VideoInfoResultVO(video, userActionList);
        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/loadVideoPList")
    public ResponseVO loadVideoPList(@NotEmpty String videoId) {
        VideoFileQuery videoFileQuery = new VideoFileQuery();
        videoFileQuery.setVideoId(videoId);
        videoFileQuery.setOrderBy("file_index asc");
        List<VideoFile> videoFileList = videoFileService.findListByParam(videoFileQuery);

        return getSuccessResponseVo(videoFileList);
    }

    @RequestMapping("reportVideoPlayOnline")
    public ResponseVO reportVideoPlayOnline(@NotEmpty String fileId, @NotEmpty String deviceId) {
        return getSuccessResponseVo(redisComponent.reportVideoPlayOnline(fileId, deviceId));
    }
}
