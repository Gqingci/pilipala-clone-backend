package com.pilipala.web.controller;

import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.CommentTopTypeEnum;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.po.UserAction;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.po.VideoComment;
import com.pilipala.entity.query.UserActionQuery;
import com.pilipala.entity.query.VideoCommentQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.entity.vo.VideoCommentResultVO;
import com.pilipala.mappers.VideoCommentMapper;
import com.pilipala.service.UserActionService;
import com.pilipala.service.VideoCommentService;
import com.pilipala.service.VideoService;
import com.pilipala.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
@Validated
@Slf4j
public class VideoCommentController extends ABaseController {
    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private UserActionService userActionService;

    @Resource
    private VideoService videoService;

    @RequestMapping("/postComment")
    public ResponseVO postComment(@NotEmpty String videoId,
                                  @NotEmpty @Size(max = 500) String content,
                                  Integer replyCommentId,
                                  @Size(max = 50) String imgPath) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        VideoComment videoComment = new VideoComment();
        videoComment.setUserId(userTokenInfoDTO.getId());
        videoComment.setUsername(userTokenInfoDTO.getUsername());
        videoComment.setAvatar(userTokenInfoDTO.getAvatar());
        videoComment.setVideoId(videoId);
        videoComment.setContent(content);
        videoComment.setImgPath(imgPath);
        videoCommentService.postComment(videoComment, replyCommentId);
        return getSuccessResponseVo(videoComment);
    }

    @RequestMapping("/loadComment")
    public ResponseVO loadComment(@NotEmpty String videoId,
                                  Integer pageNo,
                                  Integer orderType) {
        Video video = videoService.getByVideoId(videoId);
        if (video.getInteraction() != null && video.getInteraction().contains(Constants.ZERO.toString())) {
            return getSuccessResponseVo(new ArrayList<>());
        }
        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(videoId);
        videoCommentQuery.setLoadChildren(true);
        videoCommentQuery.setPageNo(pageNo);
        videoCommentQuery.setPageSize(PageSize.SIZE15.getSize());
        videoCommentQuery.setPCommentId(0);
        String orderBy = orderType == null || orderType == 0 ? "like_count desc, comment_id desc" : "comment_id desc";
        videoCommentQuery.setOrderBy(orderBy);

        PaginationResultVO<VideoComment> result = videoCommentService.findListByPage(videoCommentQuery);

        if (pageNo == null) {
            List<VideoComment> topCommentList = topCommentList(videoId);
            if (!topCommentList.isEmpty()) {
                List<VideoComment> commentList = result
                        .getList()
                        .stream()
                        .filter(item -> !item.getCommentId().equals(topCommentList.get(0).getCommentId()))
                        .collect(Collectors.toList());
                commentList.addAll(0, topCommentList);
                result.setList(commentList);
            }
        }

        VideoCommentResultVO videoCommentResultVO = new VideoCommentResultVO();
        videoCommentResultVO.setCommentData(result);

        List<UserAction> userActionList = new ArrayList<>();
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        if (userTokenInfoDTO != null) {
            UserActionQuery userActionQuery = new UserActionQuery();
            userActionQuery.setUserId(userTokenInfoDTO.getId());
            userActionQuery.setVideoId(videoId);
            userActionQuery.setActionTypeArray(new Integer[]{
                    UserActionTypeEnum.COMMENT_LIKE.getType(),
                    UserActionTypeEnum.COMMENT_HATE.getType()});
            userActionList = userActionService.findListByParam(userActionQuery);
        }
        videoCommentResultVO.setUserActionList(userActionList);

        return getSuccessResponseVo(videoCommentResultVO);
    }

    @RequestMapping("/topComment")
    public ResponseVO topComment(@NotNull Integer commentId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        videoCommentService.topComment(commentId, userTokenInfoDTO.getId());

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/cancelTopComment")
    public ResponseVO cancelTopComment(@NotNull Integer commentId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        videoCommentService.cancelTopComment(commentId, userTokenInfoDTO.getId());

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/deleteComment")
    public ResponseVO deleteComment(@NotNull Integer commentId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        videoCommentService.deleteComment(commentId, userTokenInfoDTO.getId());
        return getSuccessResponseVo(null);
    }

    private List<VideoComment> topCommentList(String videoId) {
        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(videoId);
        videoCommentQuery.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentQuery.setLoadChildren(true);
        return videoCommentService.findListByParam(videoCommentQuery);
    }
}
