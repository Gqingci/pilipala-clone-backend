package com.pilipala.service;

import com.pilipala.dto.LoginFormDTO;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.query.UsersQuery;
import com.pilipala.entity.po.Users;
import com.pilipala.entity.vo.PaginationResultVO;
import java.util.List;
/**
 * @author Gqingci
 * @Description: 对应的Service
 * @date: 2025/09/15
 */

public interface UsersService{

	/**
	 * 根据条件查询列表
	 */
	List<Users>findListByParam(UsersQuery query);

	/**
	 * 根据条件查询数量
	 */
    Integer findCountByParam(UsersQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Users> findListByPage(UsersQuery query );

	/**
	 * 新增
	 */
	Integer add(Users bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<Users> listBean);
	/**
	 * 新增或修改
	 */
	Integer addOrUpdate(Users bean);
	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<Users> listBean);
	/**
	 * 根据Id查询
	 */
	 Users getById(String id);

	/**
	 * 根据Id查询
	 */
	 Integer updateById(Users bean , String id);

	/**
	 * 根据Id删除
	 */
	 Integer deleteById(String id);

	/**
	 * 根据Username查询
	 */
	 Users getByUsername(String username);

	/**
	 * 根据Username查询
	 */
	 Integer updateByUsername(Users bean , String username);

	/**
	 * 根据Username删除
	 */
	 Integer deleteByUsername(String username);

	/**
	 * 根据Email查询
	 */
	 Users getByEmail(String email);

	/**
	 * 根据Email查询
	 */
	 Integer updateByEmail(Users bean , String email);

	/**
	 * 根据Email删除
	 */
	 Integer deleteByEmail(String email);


	void register(String email, String registerPassword);

	UserTokenInfoDTO login(LoginFormDTO loginForm, String ip);
}