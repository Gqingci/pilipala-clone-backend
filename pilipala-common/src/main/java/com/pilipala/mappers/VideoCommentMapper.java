package com.pilipala.mappers;

import com.pilipala.entity.po.VideoComment;
import com.pilipala.entity.query.VideoCommentQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author Gqingci
 * @Description: 评论的Mapper类
 * @date: 2025/10/05
 */

@Mapper
public interface VideoCommentMapper<T,P> extends BaseMapper {
	/**
	 * 根据CommentId查询
	 */
	 T selectByCommentId(@Param("commentId") Integer commentId);

	/**
	 * 根据CommentId更新
	 */
	 Integer updateByCommentId(@Param("bean") T t, @Param("commentId") Integer commentId);

	/**
	 * 根据CommentId删除
	 */
	 Integer deleteByCommentId(@Param("commentId") Integer commentId);

    Integer selectListWithChildren(@Param("query") P p);

	void updateCount(@Param("commentId") Integer commentId,
					 @Param("fieldId") String fieldId,
					 @Param("changeCount") Integer changeCount,
					 @Param("opposeField") String opposeField,
					 @Param("opposeChangeCount") Integer opposeChangeCount);

	/**
	 * 通用条件更新
	 */
	Integer updateByParam(@Param("bean") VideoComment bean, @Param("query") VideoCommentQuery query);

	// 根据条件删除（通用删除）
	Integer deleteByParam(@Param("query") P query);

}