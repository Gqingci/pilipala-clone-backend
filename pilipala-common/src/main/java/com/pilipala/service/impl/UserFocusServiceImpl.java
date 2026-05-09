package com.pilipala.service.impl;

import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.po.Users;
import com.pilipala.entity.query.UserFocusQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.UserFocus;
import com.pilipala.entity.query.UsersQuery;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.UserFocusMapper;
import com.pilipala.mappers.UsersMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.UserFocusService;
/**
 * @author Gqingci
 * @Description: 对应的ServiceImpl
 * @date: 2025/10/24
 */

@Service("userFocusService")
public class UserFocusServiceImpl implements UserFocusService{

	@Resource
	private UserFocusMapper<UserFocus,UserFocusQuery> userFocusMapper;

	@Resource
	private UsersMapper<Users, UsersQuery> usersMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserFocus>findListByParam(UserFocusQuery query){
		return this.userFocusMapper.selectList(query);
	 }
	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(UserFocusQuery query){
		return this.userFocusMapper.selectCount(query);
	 }
	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<UserFocus> findListByPage(UserFocusQuery query ){
		Integer count = this.findCountByParam(query); 
		Integer pageSize=query.getPageSize()==null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<UserFocus> list = this.findListByParam(query);
		PaginationResultVO<UserFocus> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	 }
	/**
	 * 新增
	 */
	@Override
	public Integer add(UserFocus bean){
		return this.userFocusMapper.insert(bean);
	 }
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserFocus> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.userFocusMapper.insertBatch(listBean);
	 }
	/**
	 * 新增或者修改
	 */
	@Override
	public Integer addOrUpdate(UserFocus userFocus){
		return this.userFocusMapper.insertOrUpdate(userFocus);
	 }
	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserFocus> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.userFocusMapper.insertOrUpdateBatch(listBean);
	 }
	/**
	 * 根据UserIdAndFocusUserId查询
	 */
	@Override
	 public UserFocus getByUserIdAndFocusUserId(String userId,String focusUserId){
		return this.userFocusMapper.selectByUserIdAndFocusUserId(userId,focusUserId);
	 }
	/**
	 * 根据UserIdAndFocusUserId更新
	 */
	@Override
	 public Integer updateByUserIdAndFocusUserId(UserFocus bean , String userId,String focusUserId){
		return this.userFocusMapper.updateByUserIdAndFocusUserId(bean,userId,focusUserId);
	 }
	/**
	 * 根据UserIdAndFocusUserId删除
	 */
	@Override
	 public Integer deleteByUserIdAndFocusUserId(String userId,String focusUserId){
		return this.userFocusMapper.deleteByUserIdAndFocusUserId(userId,focusUserId);
	 }

	@Override
	public void focusUser(String userId, String focusUserId) {
		if(userId.equals(focusUserId)) {
			throw new BusinessException("不能对自己进行操作");
		}

		UserFocus focusDB = this.userFocusMapper.selectByUserIdAndFocusUserId(userId, focusUserId);
		if(focusDB != null) {
			return;
		}

		Users user =  usersMapper.selectById(focusUserId);
		if(user == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		UserFocus focus = new UserFocus();
		focus.setUserId(userId);
		focus.setFocusUserId(focusUserId);
		focus.setFocusTime(new Date());
		this.userFocusMapper.insert(focus);
 	}

	@Override
	public void cancelFocusUser(String userId, String focusUserId) {
		this.userFocusMapper.deleteByUserIdAndFocusUserId(userId, focusUserId);
	}
}