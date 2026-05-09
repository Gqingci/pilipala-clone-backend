package com.pilipala.web.controller;

import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.po.VideoPlayHistory;
import com.pilipala.entity.query.VideoPlayHistoryQuery;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.VideoPlayHistoryService;
import com.pilipala.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/history")
@Slf4j
public class VideoPlayHistoryController extends ABaseController {
    @Resource
    private VideoPlayHistoryService videoPlayHistoryService;

    @RequestMapping("/loadHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadHistory(Integer pageNo) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO()   ;
        VideoPlayHistoryQuery query = new VideoPlayHistoryQuery();
        query.setUserId(userTokenInfoDTO.getId());
        query.setOrderBy("last_update_time desc");
        query.setPageNo(pageNo);
        query.setQueryVideoDetail(true);

        return getSuccessResponseVo(videoPlayHistoryService.findListByParam(query));
    }

    @RequestMapping("cleanHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO cleanHistory() {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        VideoPlayHistoryQuery query = new VideoPlayHistoryQuery();
        query.setUserId(userTokenInfoDTO.getId());
        videoPlayHistoryService.deleteByParam(query);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("delHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delHistory(@NotEmpty String videoId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        videoPlayHistoryService.deleteByUserIdAndVideoId(userTokenInfoDTO.getId(), videoId);
        return getSuccessResponseVo(null);
    }
}
