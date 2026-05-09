package com.pilipala.admin.controller;

import com.pilipala.entity.po.VideoComment;
import com.pilipala.entity.po.VideoDanmu;
import com.pilipala.entity.query.VideoCommentQuery;
import com.pilipala.entity.query.VideoDanmuQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.VideoCommentService;
import com.pilipala.service.VideoDanmuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/interact")
@Validated
@Slf4j
public class InteractController extends ABaseController {
    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private VideoDanmuService videoDanmuService;

    @RequestMapping("/loadComment")
    public ResponseVO loadComment(Integer PageNo, String videoNameFuzzy) {
        VideoCommentQuery query = new VideoCommentQuery();
        query.setPageNo(PageNo);
        query.setQueryVideoInfo(true);
        query.setVideoNameFuzzy(videoNameFuzzy);
        query.setOrderBy("comment_id desc");
        PaginationResultVO<VideoComment> resultVO = videoCommentService.findListByPage(query);
        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/delComment")
    public ResponseVO delComment(@NotNull Integer commentId) {
        videoCommentService.deleteComment(commentId, null);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(Integer PageNo, String videoNameFuzzy) {
        VideoDanmuQuery query = new VideoDanmuQuery();
        query.setPageNo(PageNo);
        query.setQueryVideoInfo(true);
        query.setVideoNameFuzzy(videoNameFuzzy);
        query.setOrderBy("danmu_id desc");
        PaginationResultVO<VideoDanmu> resultVO = videoDanmuService.findListByPage(query);
        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/delDanmu")
    public ResponseVO delDanmu(@NotNull Integer danmuId) {
        videoDanmuService.deleteDanmu(danmuId, null);
        return getSuccessResponseVo(null);
    }
}
