package com.pilipala.admin.controller;

import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.VideoStatusEnum;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.query.VideoPostQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.entity.vo.VideoStatusCountVO;
import com.pilipala.service.VideoPostService;
import com.pilipala.service.VideoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/video")
@Validated
public class VideoController extends ABaseController {
    @Resource
    private VideoPostService videoPostService;

    @Resource
    private VideoFilePost videoFilePost;

    @Resource
    private VideoService videoService;

    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(VideoPostQuery query) {
        query.setOrderBy("video_post.last_update_time desc");
        query.setQueryCountInfo(true);
        query.setQueryUserInfo(true);
        PaginationResultVO resultVO = videoPostService.findListByPage(query);

        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/auditVideo")
    public ResponseVO auditVideo(@NotEmpty String videoId, @NotNull Integer status, String reason) {
        videoPostService.auditVideo(videoId, status, reason);

        return getSuccessResponseVo(null);
    }
}
