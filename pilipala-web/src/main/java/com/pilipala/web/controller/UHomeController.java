package com.pilipala.web.controller;

import com.pilipala.web.annotation.GlobalInterceptor;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.entity.enums.UserActionTypeEnum;
import com.pilipala.entity.enums.VideoOrderTypeEnum;
import com.pilipala.entity.po.Users;
import com.pilipala.entity.query.UserActionQuery;
import com.pilipala.entity.query.UserFocusQuery;
import com.pilipala.entity.query.VideoQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.entity.vo.UserVO;
import com.pilipala.service.UserActionService;
import com.pilipala.service.UserFocusService;
import com.pilipala.service.UsersService;
import com.pilipala.service.VideoService;
import com.pilipala.utils.Constants;
import com.pilipala.utils.CopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/uhome")
@Validated
@Slf4j
public class UHomeController extends ABaseController {
    @Resource
    private UsersService usersService;

    @Resource
    private VideoService videoService;

    @Resource
    private UserFocusService userFocusService;

    @Resource
    private UserActionService userActionService;

    @RequestMapping("/getUserInfo")
    public ResponseVO getUserInfo(@NotEmpty String userId) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        Users user = usersService.getUserDetail(userTokenInfoDTO == null ? null : userTokenInfoDTO.getId(), userId);
        UserVO userInfoVO = CopyUtils.copy(user, UserVO.class);
        return getSuccessResponseVo(userInfoVO);
    }

    @RequestMapping("/updateUserInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO updateUserInfo(@NotEmpty @Size(max = 20) String username,
                                     @NotEmpty @Size(max = 100) String avatar,
                                     @NotNull Integer gender,
                                     @Size(max = 10) String birthday,
                                     @Size(max = 150) String school,
                                     @Size(max = 80) String biography,
                                     @Size(max = 300) String spaceAnnouncement) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();

        Users user = new Users();
        user.setId(userTokenInfoDTO.getId());
        user.setUsername(username);
        user.setAvatar(avatar);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setSchool(school);
        user.setBiography(biography);
        user.setSpaceAnnouncement(spaceAnnouncement);

        usersService.updateUser(user, userTokenInfoDTO);

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/focus")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO focus(@NotEmpty String focusUserId) {
        userFocusService.focusUser(getUserTokenInfoDTO().getId(), focusUserId);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/cancelFocus")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO cancelFocus(@NotEmpty String focusUserId) {
        userFocusService.cancelFocusUser(getUserTokenInfoDTO().getId(), focusUserId);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/loadFocusList")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadFocusList(Integer pageNo) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UserFocusQuery userFocusQuery = new UserFocusQuery();
        userFocusQuery.setUserId(userTokenInfoDTO.getId());
        userFocusQuery.setPageNo(pageNo);
        userFocusQuery.setOrderBy("focus_time desc");
        userFocusQuery.setQueryType(Constants.ZERO);
        PaginationResultVO resultVO = userFocusService.findListByPage(userFocusQuery);

        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/loadFansList")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadFansList(Integer pageNo) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UserFocusQuery userFocusQuery = new UserFocusQuery();
        userFocusQuery.setFocusUserId(userTokenInfoDTO.getId());
        userFocusQuery.setPageNo(pageNo);
        userFocusQuery.setOrderBy("focus_time desc");
        userFocusQuery.setQueryType(Constants.ONE);
        PaginationResultVO resultVO = userFocusService.findListByPage(userFocusQuery);

        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/loadVideoList")
    @GlobalInterceptor
    public ResponseVO loadVideoList(@NotEmpty String userId,
                                    Integer type,
                                    Integer pageNo,
                                    String videoName,
                                    Integer orderType) {
        VideoQuery query = new VideoQuery();
        if (type != null) {
            query.setPageNo(PageSize.SIZE10.getSize());
        }
        VideoOrderTypeEnum videoOrderTypeEnum = VideoOrderTypeEnum.getByType(orderType);
        if (videoOrderTypeEnum == null) {
            videoOrderTypeEnum = VideoOrderTypeEnum.CREATE_TIME;
        }
        query.setPageNo(pageNo);
        query.setOrderBy(videoOrderTypeEnum.getField() + " desc");
        query.setUserId(userId);
        query.setVideoNameFuzzy(videoName);
        PaginationResultVO resultVO = videoService.findListByPage(query);
        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/loadUserCollection")
    @GlobalInterceptor
    public ResponseVO loadUserCollection(@NotEmpty String userId, Integer pageNo) {
        UserActionQuery query = new UserActionQuery();
        query.setUserId(userId);
        query.setPageNo(pageNo);
        query.setActionType(UserActionTypeEnum.VIDEO_COLLECT.getType());
        query.setOrderBy("action_time desc");
        query.setQueryVideoInfo(true);
        PaginationResultVO resultVO = userActionService.findListByPage(query);
        return getSuccessResponseVo(resultVO);
    }
}
