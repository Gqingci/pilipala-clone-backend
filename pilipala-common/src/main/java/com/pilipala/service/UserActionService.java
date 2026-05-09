package com.pilipala.service;

import com.pilipala.entity.query.UserActionQuery;
import com.pilipala.entity.po.UserAction;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 用户行为 点赞、评论对应的Service
 * @date: 2025/10/05
 */

public interface UserActionService{

	/**
	 * 根据条件查询列表
	 */
	List<UserAction>findListByParam(UserActionQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(UserActionQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserAction> findListByPage(UserActionQuery query );

	/**
	 * 新增
	 */
	Integer add(UserAction bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserAction> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(UserAction bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserAction> listBean);
	/**
	 * 根据ActionId查询
	 */
	 UserAction getByActionId(Integer actionId);

	/**
	 * 根据ActionId查询
	 */
	 Integer updateByActionId(UserAction bean , Integer actionId);

	/**
	 * 根据ActionId删除
	 */
	 Integer deleteByActionId(Integer actionId);

	/**
	 * 根据VideoIdAndCommentIdAndActionTypeAndUserId查询
	 */
	 UserAction getByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId,Integer commentId,Integer actionType,String userId);

	/**
	 * 根据VideoIdAndCommentIdAndActionTypeAndUserId查询
	 */
	 Integer updateByVideoIdAndCommentIdAndActionTypeAndUserId(UserAction bean , String videoId,Integer commentId,Integer actionType,String userId);

	/**
	 * 根据VideoIdAndCommentIdAndActionTypeAndUserId删除
	 */
	 Integer deleteByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId,Integer commentId,Integer actionType,String userId);


    void saveAction(UserAction userAction);
}