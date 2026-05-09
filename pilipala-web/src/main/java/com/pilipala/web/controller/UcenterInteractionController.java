package com.pilipala.web.controller;

import com.pilipala.web.annotation.GlobalInterceptor;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.query.VideoCommentQuery;
import com.pilipala.entity.query.VideoDanmuQuery;
import com.pilipala.entity.query.VideoQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.VideoCommentService;
import com.pilipala.service.VideoDanmuService;
import com.pilipala.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/ucenter")
@Validated
@Slf4j
public class UcenterInteractionController extends ABaseController {
    @Resource
    private VideoService videoService;

    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private VideoDanmuService videoDanmuService;

    @RequestMapping("loadAllVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadAllVideo() {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        VideoQuery query = new VideoQuery();
        query.setUserId(userTokenInfoDTO.getId());
        query.setOrderBy("create_time desc");
        List<Video> videoList = videoService.findListByParam(query);
        return getSuccessResponseVo(videoList);
    }

    @RequestMapping("loadComment")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadComment(Integer pageNo, String videoId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        VideoCommentQuery query = new VideoCommentQuery();
        query.setVideoUserId(userTokenInfoDTO.getId());
        query.setVideoId(videoId);
        query.setPageNo(pageNo);
        query.setOrderBy("comment_id desc");
        query.setQueryVideoInfo(true);
        PaginationResultVO resultVO = videoCommentService.findListByPage(query);

        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("delComment")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delComment(@NotNull Integer commentId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        videoCommentService.deleteComment(commentId, userTokenInfoDTO.getId());
        return getSuccessResponseVo(null);
    }

    @RequestMapping("loadDanmu")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadDanmu(Integer pageNo, String videoId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        VideoDanmuQuery query = new VideoDanmuQuery();
        query.setVideoUserId(userTokenInfoDTO.getId());
        query.setVideoId(videoId);
        query.setOrderBy("danmu_id asc");
        query.setPageNo(pageNo);
        query.setQueryVideoInfo(true);
        PaginationResultVO resultVO = videoDanmuService.findListByPage(query);

        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("delDanmu")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delDanmu(@NotNull Integer danmuId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        videoDanmuService.deleteDanmu(danmuId, userTokenInfoDTO.getId());
        return getSuccessResponseVo(null);
    }
}
