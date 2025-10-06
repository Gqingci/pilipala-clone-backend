package com.pilipala.service.impl;

import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.CommentTopTypeEnum;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.po.UserAction;
import com.pilipala.entity.po.Users;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.query.*;
import com.pilipala.entity.po.VideoComment;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.UserActionMapper;
import com.pilipala.mappers.UsersMapper;
import com.pilipala.mappers.VideoCommentMapper;
import com.pilipala.mappers.VideoMapper;
import com.pilipala.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.VideoCommentService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gqingci
 * @Description: 评论对应的ServiceImpl
 * @date: 2025/10/05
 */

@Service("videoCommentService")
public class VideoCommentServiceImpl implements VideoCommentService {

    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

    @Resource
    private VideoMapper<Video, VideoQuery> videoMapper;

    @Resource
    private UsersMapper<Users, UsersQuery> usersMapper;

    @Resource
    private UserActionMapper<UserAction, UserActionQuery> userActionMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<VideoComment> findListByParam(VideoCommentQuery query) {
        return this.videoCommentMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(VideoCommentQuery query) {
        if (query.getLoadChildren() != null && query.getLoadChildren()) {
            return this.videoCommentMapper.selectListWithChildren(query);
        }
        return this.videoCommentMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<VideoComment> findListByPage(VideoCommentQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<VideoComment> list = this.findListByParam(query);
        PaginationResultVO<VideoComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(VideoComment bean) {
        return this.videoCommentMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<VideoComment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoCommentMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(VideoComment videoComment) {
        return this.videoCommentMapper.insertOrUpdate(videoComment);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<VideoComment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoCommentMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据CommentId查询
     */
    @Override
    public VideoComment getByCommentId(Integer commentId) {
        return this.videoCommentMapper.selectByCommentId(commentId);
    }

    /**
     * 根据CommentId更新
     */
    @Override
    public Integer updateByCommentId(VideoComment bean, Integer commentId) {
        return this.videoCommentMapper.updateByCommentId(bean, commentId);
    }

    /**
     * 根据CommentId删除
     */
    @Override
    public Integer deleteByCommentId(Integer commentId) {
        return this.videoCommentMapper.deleteByCommentId(commentId);
    }

    /**
     * 添加评论
     */
    @Override
    public void postComment(VideoComment videoComment, Integer replyCommentId) {
        Video video = videoMapper.selectByVideoId(videoComment.getVideoId());
        if (video == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (video.getInteraction() != null && video.getInteraction().contains(Constants.ZERO.toString())) {
            throw new BusinessException("评论区已被关闭");
        }
        if (replyCommentId != null) {
            VideoComment replyComment = getByCommentId(replyCommentId);
            if (replyComment == null || !replyComment.getVideoId().equals(videoComment.getVideoId())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            if (replyComment.getCommentId() == 0) {
                videoComment.setPCommentId(replyComment.getCommentId());
            } else {
                videoComment.setPCommentId(replyComment.getPCommentId());
                videoComment.setReplyUserId(replyComment.getUserId());
            }
            Users user = usersMapper.selectById(replyComment.getUserId());
            videoComment.setReplyUsername(user.getUsername());
            videoComment.setReplyAvatar(user.getAvatar());
        } else {
            videoComment.setCommentId(0);
        }

        videoComment.setPostTime(new Date());
        videoComment.setVideoUserId(video.getUserId());
        this.videoCommentMapper.insert(videoComment);
        this.videoMapper.updateCount(videoComment.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), Constants.ONE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void topComment(Integer commentId, String id) {
        this.cancelTopComment(commentId, id);
        VideoComment videoComment = new VideoComment();
        videoComment.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentMapper.updateByCommentId(videoComment, commentId);
    }

    @Override
    public void cancelTopComment(Integer commentId, String id) {
        VideoComment videoCommentDB = videoCommentMapper.selectByCommentId(commentId);
        if (videoCommentDB == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Video video = videoMapper.selectByVideoId(videoCommentDB.getVideoId());
        if (video == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!video.getUserId().equals(id)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        VideoComment videoComment = new VideoComment();
        videoComment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(videoCommentDB.getVideoId());
        videoCommentQuery.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentMapper.updateByParam(videoComment, videoCommentQuery);
    }

    @Override
    public void deleteComment(Integer commentId, String id) {
        VideoComment videoComment = videoCommentMapper.selectByCommentId(commentId);
        if (videoComment == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Video video = videoMapper.selectByVideoId(videoComment.getVideoId());
        if (video == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if(!video.getUserId().equals(id) || !videoComment.getUserId().equals(id)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        // 删除评论的数量
        int deleteCount = 0;

        if(videoComment.getPCommentId() == 0) {
            VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
            videoCommentQuery.setPCommentId(commentId);
            // 删除子评论
            deleteCount += videoCommentMapper.deleteByParam(videoCommentQuery);
        }
        // 删除父评论
        videoCommentMapper.deleteByCommentId(commentId);
        deleteCount += 1;
        // 减去视频的评论数
        videoMapper.updateCount(videoComment.getVideoId(),
                UserActionTypeEnum.VIDEO_COMMENT.getField(),
                -deleteCount);
    }
}