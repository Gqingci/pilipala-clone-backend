package com.pilipala.web.controller;

import com.pilipala.web.annotation.GlobalInterceptor;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.po.UserVideoSeries;
import com.pilipala.entity.po.UserVideoSeriesVideo;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.query.*;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.entity.vo.UserVideoSeriesDetailVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/uhome/series")
@Validated
@Slf4j
public class UHomeVideoSeriesController extends ABaseController {


    @Resource
    private VideoService videoService;

    @Resource
    private UserVideoSeriesService userVideoSeriesService;

    @Resource
    private UserVideoSeriesVideoService userVideoSeriesVideoService;

    @RequestMapping("/saveVideoSeries")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO saveVideoSeries(Integer seriesId,
                                      @NotEmpty @Size(max = 100) String seriesName,
                                      @Size(max = 200) String seriesDescription,
                                      String videoIds) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();

        UserVideoSeries userVideoSeries = new UserVideoSeries();
        userVideoSeries.setUserId(userTokenInfoDTO.getId());
        userVideoSeries.setSeriesId(seriesId);
        userVideoSeries.setSeriesName(seriesName);
        userVideoSeries.setSeriesDescription(seriesDescription);

        this.userVideoSeriesService.saveUserVideoSeries(userVideoSeries, videoIds);
        return getSuccessResponseVo(userVideoSeries);
    }

    @RequestMapping("/loadVideoSeriesList")
    @GlobalInterceptor
    public ResponseVO loadVideoSeriesList(@NotEmpty String userId) {
        List<UserVideoSeries> userVideoSeriesList = userVideoSeriesService.getUserAllSeries(userId);
        return getSuccessResponseVo(userVideoSeriesList);
    }

    @RequestMapping("/loadAllVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadAllVideo(Integer seriesId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        VideoQuery query = new VideoQuery();
        if(seriesId != null) {
            UserVideoSeriesVideoQuery userVideoSeriesVideoQuery = new UserVideoSeriesVideoQuery();
            userVideoSeriesVideoQuery.setSeriesId(seriesId);
            userVideoSeriesVideoQuery.setUserId(userTokenInfoDTO.getId());
            List<UserVideoSeriesVideo> seriesVideoList = userVideoSeriesVideoService.findListByParam(userVideoSeriesVideoQuery);
            List<String> videoIdList = seriesVideoList
                    .stream()
                    .map(UserVideoSeriesVideo::getVideoId)
                    .collect(Collectors.toList());
            query.setExcludeVideoIdArray(videoIdList.toArray(new String[videoIdList.size()]));
        }
        query.setUserId(userTokenInfoDTO.getId());
        List<Video> videoList = videoService.findListByParam(query);
        return getSuccessResponseVo(videoList);
    }

    @RequestMapping("/getVideoSeriesDetail")
    @GlobalInterceptor
    public ResponseVO getVideoSeriesDetail(@NotNull Integer seriesId) {
        UserVideoSeries userVideoSeries = userVideoSeriesService.getBySeriesId(seriesId);

        if(videoService == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }

        UserVideoSeriesVideoQuery query = new UserVideoSeriesVideoQuery();
        query.setOrderBy("sort asc");
        query.setSeriesId(seriesId);
        query.setQueryVideoInfo(true);
        List<UserVideoSeriesVideo> seriesVideoList = userVideoSeriesVideoService.findListByParam(query);

        return getSuccessResponseVo(new UserVideoSeriesDetailVO(userVideoSeries, seriesVideoList));
    }

    @RequestMapping("saveSeriesVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO saveSeriesVideo(@NotNull Integer seriesId,
                                      @NotEmpty @Size(max = 1000) String videoIds) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        this.userVideoSeriesService.saveSeriesVideo(userTokenInfoDTO.getId(), seriesId, videoIds);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("delSeriesVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delSeriesVideo(@NotNull Integer seriesId,
                                     @NotEmpty String videoId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        this.userVideoSeriesService.delSeriesVideo(userTokenInfoDTO.getId(), seriesId, videoId);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("delVideoSeries")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delVideoSeries(@NotNull Integer seriesId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        this.userVideoSeriesService.delVideoSeries(userTokenInfoDTO.getId(), seriesId);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("changeVideoSeriesSort")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO changeVideoSeriesSort(@NotNull String seriesIds) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        this.userVideoSeriesService.changeVideoSeriesSort(userTokenInfoDTO.getId(), seriesIds);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("loadVideoSeriesWithVideo")
    @GlobalInterceptor
    public ResponseVO loadVideoSeriesWithVideo(@NotEmpty String userId) {
        UserVideoSeriesQuery query = new UserVideoSeriesQuery();
        query.setUserId(userId);
        query.setOrderBy("sort asc");
        List<UserVideoSeries> videoSeries = userVideoSeriesService.findListWithVideoList(query);
        return getSuccessResponseVo(videoSeries);
    }
}
