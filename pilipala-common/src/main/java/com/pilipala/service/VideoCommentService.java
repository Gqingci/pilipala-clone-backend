package com.pilipala.service;

import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.query.VideoCommentQuery;
import com.pilipala.entity.po.VideoComment;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 评论对应的Service
 * @date: 2025/10/05
 */

public interface VideoCommentService{

	/**
	 * 根据条件查询列表
	 */
	List<VideoComment>findListByParam(VideoCommentQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(VideoCommentQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoComment> findListByPage(VideoCommentQuery query );

	/**
	 * 新增
	 */
	Integer add(VideoComment bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoComment> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(VideoComment bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<VideoComment> listBean);
	/**
	 * 根据CommentId查询
	 */
	 VideoComment getByCommentId(Integer commentId);

	/**
	 * 根据CommentId查询
	 */
	 Integer updateByCommentId(VideoComment bean , Integer commentId);

	/**
	 * 根据CommentId删除
	 */
	 Integer deleteByCommentId(Integer commentId);


	void postComment(VideoComment videoComment, Integer replyCommentId);

	void topComment(Integer commentId, String id);

	void cancelTopComment(Integer commentId, String id);

	void deleteComment(Integer commentId, String id);
}