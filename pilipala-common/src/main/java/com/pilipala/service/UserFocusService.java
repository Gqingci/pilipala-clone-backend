package com.pilipala.service;

import com.pilipala.entity.query.UserFocusQuery;
import com.pilipala.entity.po.UserFocus;
import com.pilipala.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * @author Gqingci
 * @Description: 对应的Service
 * @date: 2025/10/24
 */

public interface UserFocusService {

    /**
     * 根据条件查询列表
     */
    List<UserFocus> findListByParam(UserFocusQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(UserFocusQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<UserFocus> findListByPage(UserFocusQuery query);

    /**
     * 新增
     */
    Integer add(UserFocus bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserFocus> listBean);

    /**
     * 新增或修改
     */
    Integer addOrUpdate(UserFocus bean);

    /**
     * 批量新增或修改
     */
    Integer addOrUpdateBatch(List<UserFocus> listBean);

    /**
     * 根据UserIdAndFocusUserId查询
     */
    UserFocus getByUserIdAndFocusUserId(String userId, String focusUserId);

    /**
     * 根据UserIdAndFocusUserId查询
     */
    Integer updateByUserIdAndFocusUserId(UserFocus bean, String userId, String focusUserId);

    /**
     * 根据UserIdAndFocusUserId删除
     */
    Integer deleteByUserIdAndFocusUserId(String userId, String focusUserId);


    void focusUser(String userId, String focusUserId);

    void cancelFocusUser(String userId, String focusUserId);
}