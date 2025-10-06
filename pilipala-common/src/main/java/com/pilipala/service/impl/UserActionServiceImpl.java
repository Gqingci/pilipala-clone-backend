package com.pilipala.service.impl;

import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.po.Users;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.po.VideoComment;
import com.pilipala.entity.query.*;
import com.pilipala.entity.po.UserAction;
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
import com.pilipala.service.UserActionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gqingci
 * @Description: 用户行为 点赞、评论对应的ServiceImpl
 * @date: 2025/10/05
 */

@Service("userActionService")
public class UserActionServiceImpl implements UserActionService {

    @Resource
    private UserActionMapper<UserAction, UserActionQuery> userActionMapper;

    @Resource
    private VideoMapper<Video, VideoQuery> videoMapper;

    @Resource
    private UsersMapper<Users, UsersQuery> usersMapper;

    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<UserAction> findListByParam(UserActionQuery query) {
        return this.userActionMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(UserActionQuery query) {
        return this.userActionMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<UserAction> findListByPage(UserActionQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserAction> list = this.findListByParam(query);
        PaginationResultVO<UserAction> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(UserAction bean) {
        return this.userActionMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<UserAction> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userActionMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(UserAction userAction) {
        return this.userActionMapper.insertOrUpdate(userAction);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<UserAction> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userActionMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据ActionId查询
     */
    @Override
    public UserAction getByActionId(Integer actionId) {
        return this.userActionMapper.selectByActionId(actionId);
    }

    /**
     * 根据ActionId更新
     */
    @Override
    public Integer updateByActionId(UserAction bean, Integer actionId) {
        return this.userActionMapper.updateByActionId(bean, actionId);
    }

    /**
     * 根据ActionId删除
     */
    @Override
    public Integer deleteByActionId(Integer actionId) {
        return this.userActionMapper.deleteByActionId(actionId);
    }

    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId查询
     */
    @Override
    public UserAction getByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId) {
        return this.userActionMapper.selectByVideoIdAndCommentIdAndActionTypeAndUserId(videoId, commentId, actionType, userId);
    }

    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId更新
     */
    @Override
    public Integer updateByVideoIdAndCommentIdAndActionTypeAndUserId(UserAction bean, String videoId, Integer commentId, Integer actionType, String userId) {
        return this.userActionMapper.updateByVideoIdAndCommentIdAndActionTypeAndUserId(bean, videoId, commentId, actionType, userId);
    }

    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId删除
     */
    @Override
    public Integer deleteByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId) {
        return this.userActionMapper.deleteByVideoIdAndCommentIdAndActionTypeAndUserId(videoId, commentId, actionType, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAction(UserAction userAction) {
        Video video = videoMapper.selectByVideoId(userAction.getVideoId());
        if (video == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        userAction.setVideoUserId(video.getUserId());

        UserActionTypeEnum userActionTypeEnum = UserActionTypeEnum.getByType(userAction.getActionType());
        if (userActionTypeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        UserAction actionDB = userActionMapper
                .selectByVideoIdAndCommentIdAndActionTypeAndUserId(
                        userAction.getVideoId(),
                        userAction.getCommentId(),
                        userAction.getActionType(),
                        userAction.getUserId());
        userAction.setActionTime(new Date());

        switch (userActionTypeEnum) {
            case VIDEO_LIKE:
            case VIDEO_COLLECT:
                if (actionDB != null) {
                    userActionMapper.deleteByActionId(actionDB.getActionId());
                } else {
                    userActionMapper.insert(userAction);
                }
                Integer changeCount = actionDB == null ? Constants.ONE : userAction.getActionCount();
                videoMapper.updateCount(video.getVideoId(), userActionTypeEnum.getField(), changeCount);
                if (userActionTypeEnum == UserActionTypeEnum.VIDEO_COLLECT.VIDEO_LIKE) {
                    // TODO 更新es的收藏数量
                }
                break;
            case VIDEO_COIN:
                if (video.getUserId().equals(userAction.getUserId())) {
                    throw new BusinessException("不可以给自己投币");
                }
                if (actionDB != null) {
                    throw new BusinessException("不可以重复投币");
                }

                // 扣除用户硬币
                Integer updateCount = usersMapper.updateCoinCount(userAction.getUserId(), -userAction.getActionCount());
                if (updateCount == 0) {
                    throw new BusinessException("硬币数量不足");
                }

                // 给UP主添加硬币
                updateCount = usersMapper.updateCoinCount(video.getUserId(), userAction.getActionCount());
                if (updateCount == 0) {
                    throw new BusinessException("投币失败~");
                }
                userActionMapper.insert(userAction);

                videoMapper.updateCount(userAction.getVideoId(), userActionTypeEnum.getField(), userAction.getActionCount());
                break;
            case COMMENT_LIKE:
            case COMMENT_HATE:
                UserActionTypeEnum opposeTypeEnum = userActionTypeEnum == UserActionTypeEnum.COMMENT_LIKE
                        ? UserActionTypeEnum.COMMENT_HATE
                        : UserActionTypeEnum.COMMENT_LIKE;

                UserAction opposeAction = userActionMapper
                        .selectByVideoIdAndCommentIdAndActionTypeAndUserId(
                                userAction.getVideoId(),
                                userAction.getCommentId(),
                                opposeTypeEnum.getType(),
                                userAction.getUserId());
                if (opposeAction != null) {
                    userActionMapper.deleteByActionId(opposeAction.getActionId());
                }

                if (actionDB != null) {
                    userActionMapper.deleteByActionId(actionDB.getActionId());
                } else {
                    userActionMapper.insert(userAction);
                }

                changeCount = actionDB == null ? Constants.ONE : -Constants.ONE;
                Integer opposeChangeCount = -changeCount;
                videoCommentMapper.updateCount(userAction.getCommentId(),
                        userActionTypeEnum.getField(),
                        changeCount,
                        opposeAction == null ? null : opposeTypeEnum.getField(),
                        opposeChangeCount);
                break;
        }
    }
}