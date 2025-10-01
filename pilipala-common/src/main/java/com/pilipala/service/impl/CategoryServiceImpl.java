package com.pilipala.service.impl;

import com.pilipala.entity.query.CategoryQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.Category;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.mappers.CategoryMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;
import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.CategoryService;
/**
 * @author Gqingci
 * @Description: 分类信息对应的ServiceImpl
 * @date: 2025/10/01
 */

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{

	@Resource
	private CategoryMapper<Category,CategoryQuery> categoryMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Category>findListByParam(CategoryQuery query){
		return this.categoryMapper.selectList(query);
	 }
	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(CategoryQuery query){
		return this.categoryMapper.selectCount(query);
	 }
	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Category> findListByPage(CategoryQuery query ){
		Integer count = this.findCountByParam(query); 
		Integer pageSize=query.getPageSize()==null? PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<Category> list = this.findListByParam(query);
		PaginationResultVO<Category> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	 }
	/**
	 * 新增
	 */
	@Override
	public Integer add(Category bean){
		return this.categoryMapper.insert(bean);
	 }
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Category> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.categoryMapper.insertBatch(listBean);
	 }
	/**
	 * 新增或者修改
	 */
	@Override
	public Integer addOrUpdate(Category category){
		return this.categoryMapper.insertOrUpdate(category);
	 }
	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Category> listBean){
		if(listBean==null || listBean.isEmpty()){
			return 0;
		}
		return this.categoryMapper.insertOrUpdateBatch(listBean);
	 }
	/**
	 * 根据CategoryId查询
	 */
	@Override
	 public Category getByCategoryId(Integer categoryId){
		return this.categoryMapper.selectByCategoryId(categoryId);
	 }
	/**
	 * 根据CategoryId更新
	 */
	@Override
	 public Integer updateByCategoryId(Category bean , Integer categoryId){
		return this.categoryMapper.updateByCategoryId(bean,categoryId);
	 }
	/**
	 * 根据CategoryId删除
	 */
	@Override
	 public Integer deleteByCategoryId(Integer categoryId){
		return this.categoryMapper.deleteByCategoryId(categoryId);
	 }
	/**
	 * 根据CategoryCode查询
	 */
	@Override
	 public Category getByCategoryCode(String categoryCode){
		return this.categoryMapper.selectByCategoryCode(categoryCode);
	 }
	/**
	 * 根据CategoryCode更新
	 */
	@Override
	 public Integer updateByCategoryCode(Category bean , String categoryCode){
		return this.categoryMapper.updateByCategoryCode(bean,categoryCode);
	 }
	/**
	 * 根据CategoryCode删除
	 */
	@Override
	 public Integer deleteByCategoryCode(String categoryCode){
		return this.categoryMapper.deleteByCategoryCode(categoryCode);
	 }

}