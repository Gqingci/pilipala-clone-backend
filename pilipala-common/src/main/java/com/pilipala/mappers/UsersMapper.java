package com.pilipala.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author Gqingci
 * @Description: 的Mapper类
 * @date: 2025/09/15
 */

@Mapper
public interface UsersMapper<T,P> extends BaseMapper {
	/**
	 * 根据Id查询
	 */
	 T selectById(@Param("id") String id);

	/**
	 * 根据Id更新
	 */
	 Integer updateById(@Param("bean") T t, @Param("id") String id);

	/**
	 * 根据Id删除
	 */
	 Integer deleteById(@Param("id") String id);

	/**
	 * 根据Username查询
	 */
	 T selectByUsername(@Param("username") String username);

	/**
	 * 根据Username更新
	 */
	 Integer updateByUsername(@Param("bean") T t, @Param("username") String username);

	/**
	 * 根据Username删除
	 */
	 Integer deleteByUsername(@Param("username") String username);

	/**
	 * 根据Email查询
	 */
	 T selectByEmail(@Param("email") String email);

	/**
	 * 根据Email更新
	 */
	 Integer updateByEmail(@Param("bean") T t, @Param("email") String email);

	/**
	 * 根据Email删除
	 */
	 Integer deleteByEmail(@Param("email") String email);

	 Integer updateCoinCount(@Param("id") String id, @Param("changeCount") Integer changeCount);
}