package com.pilipala.admin.controller;

import com.pilipala.entity.query.UsersQuery;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController extends ABaseController {
    @Resource
    private UsersService usersService;

    @RequestMapping("/loadUser")
    public ResponseVO loadUser(UsersQuery query) {
        query.setOrderBy("join_date desc");
        return getSuccessResponseVo(usersService.findListByPage(query));
    }

    @RequestMapping("/changeStatus")
    public ResponseVO changeStatus(@NotNull String userId, Integer isActive) {
        usersService.changeUserStatus(userId, isActive);
        return getSuccessResponseVo(null);
    }
}
