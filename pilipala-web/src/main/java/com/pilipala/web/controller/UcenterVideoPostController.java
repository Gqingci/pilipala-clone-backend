package com.pilipala.web.controller;

import com.pilipala.component.RedisComponent;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.VideoStatusEnum;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.po.VideoPost;
import com.pilipala.entity.query.VideoPostQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.entity.vo.VideoStatusCountVO;
import com.pilipala.service.VideoFilePostService;
import com.pilipala.service.VideoPostService;
import com.pilipala.service.VideoService;
import com.pilipala.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/Ucenter")
@Validated
@Slf4j
public class UcenterVideoPostController extends ABaseController {
    @Resource
    private VideoPostService videoPostService;

    @Resource
    private VideoFilePostService videoFilePostService;

    @Resource
    private VideoService videoService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/postVideo")
    public ResponseVO postVideo(String videoId,
                                @NotEmpty String videoCover,
                                @NotEmpty @Size(max = 100) String videoName,
                                @NotNull Integer pCategoryId,
                                Integer categoryId,
                                @NotNull Integer postType,
                                @NotEmpty @Size(max = 300) String tags,
                                @Size(max = 2000) String introduction,
                                @Size(max = 3) String interaction,
                                @NotEmpty String uploadFileList) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        List<VideoFilePost> videoFilePostList = JsonUtils.convertJsonArray2List(uploadFileList, VideoFilePost.class);

        VideoPost videoPost = new VideoPost();
        videoPost.setVideoId(videoId);
        videoPost.setVideoCover(videoCover);
        videoPost.setVideoName(videoName);
        videoPost.setPCategoryId(pCategoryId);
        videoPost.setCategoryId(categoryId);
        videoPost.setPostType(postType);
        videoPost.setTags(tags);
        videoPost.setIntroduction(introduction);
        videoPost.setInteraction(interaction);

        videoPost.setUserId(userTokenInfoDTO.getId());

        videoPostService.saveVideo(videoPost, videoFilePostList);

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(Integer status, Integer pageNo, String videoNameFuzzy) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();

        VideoPostQuery query = new VideoPostQuery();
        query.setUserId(userTokenInfoDTO.getId());
        query.setOrderBy("video_post.create_time desc");
        query.setPageNo(pageNo);
        if (status == -1) {
            query.setExcludeStatusArray(new Integer[]
                    {VideoStatusEnum.STATUS3.getStatus(), VideoStatusEnum.STATUS4.getStatus()});
        } else {
            query.setStatus(status);
        }
        query.setVideoNameFuzzy(videoNameFuzzy);
        query.setQueryCountInfo(true);
        PaginationResultVO resultVO = videoPostService.findListByPage(query);
        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("getVideoCountInfo")
    public ResponseVO getVideoCountInfo() {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();

        VideoPostQuery query = new VideoPostQuery();
        query.setUserId(userTokenInfoDTO.getId());
        query.setStatus(VideoStatusEnum.STATUS3.getStatus());
        Integer auditPassCount = videoPostService.findCountByParam(query);

        query.setStatus(VideoStatusEnum.STATUS4.getStatus());
        Integer auditFailCount = videoPostService.findCountByParam(query);

        query.setStatus(null);
        query.setExcludeStatusArray(new Integer[]{VideoStatusEnum.STATUS3.getStatus(), VideoStatusEnum.STATUS4.getStatus()});
        Integer inProgressCount = videoPostService.findCountByParam(query);

        VideoStatusCountVO countVO = new VideoStatusCountVO();
        countVO.setAuditPassCount(auditPassCount);
        countVO.setAuditFailCount(auditFailCount);
        countVO.setInProgress(inProgressCount);

        return getSuccessResponseVo(countVO);
    }
}
