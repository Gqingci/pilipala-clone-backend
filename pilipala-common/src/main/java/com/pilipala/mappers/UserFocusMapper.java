package com.pilipala.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author Gqingci
 * @Description: 的Mapper类
 * @date: 2025/10/24
 */

@Mapper
public interface UserFocusMapper<T,P> extends BaseMapper {
	/**
	 * 根据UserIdAndFocusUserId查询
	 */
	 T selectByUserIdAndFocusUserId(@Param("userId") String userId,@Param("focusUserId") String focusUserId);

	/**
	 * 根据UserIdAndFocusUserId更新
	 */
	 Integer updateByUserIdAndFocusUserId(@Param("bean") T t, @Param("userId") String userId,@Param("focusUserId") String focusUserId);

	/**
	 * 根据UserIdAndFocusUserId删除
	 */
	 Integer deleteByUserIdAndFocusUserId(@Param("userId") String userId,@Param("focusUserId") String focusUserId);

	 Integer selectFansCount(@Param("userId") String userId);

	 Integer selectFocusCount(@Param("userId") String userId);
}