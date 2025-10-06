package com.pilipala.web.controller;

import com.pilipala.entity.po.Video;
import com.pilipala.entity.po.VideoDanmu;
import com.pilipala.entity.query.VideoDanmuQuery;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.VideoDanmuService;
import com.pilipala.service.VideoService;
import com.pilipala.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/danmu")
@Slf4j
public class VideoDanmuController extends ABaseController {
    @Resource
    private VideoDanmuService videoDanmuService;

    @Resource
    private VideoService videoService;

    @RequestMapping("/postDanmu")
    public ResponseVO postDanmu(@NotEmpty String videoId, @NotEmpty String fileId, @NotEmpty @Size(max = 200) String text, @NotNull Integer mode, @NotEmpty String color, @NotNull Integer time) {
        VideoDanmu videoDanmu = new VideoDanmu();
        videoDanmu.setVideoId(videoId);
        videoDanmu.setFileId(fileId);
        videoDanmu.setUserId(getUserTokenInfoDTO().getId());
        videoDanmu.setText(text);
        videoDanmu.setNode(mode);
        videoDanmu.setColor(color);
        videoDanmu.setTime(time);
        videoDanmu.setPostTime(new Date());

        videoDanmuService.saveVideoDanmu(videoDanmu);

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(@NotEmpty String videoId, @NotEmpty String fileId) {
        Video video = videoService.getByVideoId(videoId);
        if (video.getInteraction() != null && video.getInteraction().contains(Constants.ONE.toString())) {
            return getSuccessResponseVo(new ArrayList<>());
        }
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setFileId(fileId);
        videoDanmuQuery.setOrderBy("danmu_id asc");

        return getSuccessResponseVo(videoDanmuService.findListByParam(videoDanmuQuery));
    }
}
