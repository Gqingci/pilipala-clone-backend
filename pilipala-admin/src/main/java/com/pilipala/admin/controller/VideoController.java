package com.pilipala.admin.controller;

import com.pilipala.annotation.RecordUserMessage;
import com.pilipala.entity.enums.MessageTypeEnum;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.query.VideoFilePostQuery;
import com.pilipala.entity.query.VideoPostQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.VideoFilePostService;
import com.pilipala.service.VideoPostService;
import com.pilipala.service.VideoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/video")
@Validated
public class VideoController extends ABaseController {
    @Resource
    private VideoPostService videoPostService;

    @Resource
    private VideoService videoService;

    @Resource
    private VideoFilePostService videoFilePostService;

    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(VideoPostQuery query) {
        query.setOrderBy("video_post.last_update_time desc");
        query.setQueryCountInfo(true);
        query.setQueryUserInfo(true);
        PaginationResultVO resultVO = videoPostService.findListByPage(query);

        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/auditVideo")
    @RecordUserMessage(messageType = MessageTypeEnum.SYS)
    public ResponseVO auditVideo(@NotEmpty String videoId, @NotNull Integer status, String reason) {
        videoPostService.auditVideo(videoId, status, reason);

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/recommendVideo")
    public ResponseVO recommendVideo(@NotEmpty String videoId) {
        videoService.recommendVideo(videoId);

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/delVideo")
    public ResponseVO delVideo(@NotEmpty String videoId) {
        videoService.deleteVideo(videoId, null);

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/loadVideoPList")
    public ResponseVO loadVideoPList(@NotEmpty String videoId) {
        VideoFilePostQuery query = new VideoFilePostQuery();
        query.setVideoId(videoId);
        query.setOrderBy("file_index asc");
        List<VideoFilePost> videoFilePostList = videoFilePostService.findListByParam(query);

        return getSuccessResponseVo(videoFilePostList);
    }
}
