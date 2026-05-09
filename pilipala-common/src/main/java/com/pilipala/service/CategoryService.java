package com.pilipala.service;

import com.pilipala.entity.query.CategoryQuery;
import com.pilipala.entity.po.Category;
import com.pilipala.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * @author Gqingci
 * @Description: 分类信息对应的Service
 * @date: 2025/10/01
 */

public interface CategoryService {

    /**
     * 根据条件查询列表
     */
    List<Category> findListByParam(CategoryQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(CategoryQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<Category> findListByPage(CategoryQuery query);

    /**
     * 新增
     */
    Integer add(Category bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<Category> listBean);

    /**
     * 新增或修改
     */
    Integer addOrUpdate(Category bean);

    /**
     * 批量新增或修改
     */
    Integer addOrUpdateBatch(List<Category> listBean);

    /**
     * 根据CategoryId查询
     */
    Category getByCategoryId(Integer categoryId);

    /**
     * 根据CategoryId查询
     */
    Integer updateByCategoryId(Category bean, Integer categoryId);

    /**
     * 根据CategoryId删除
     */
    Integer deleteByCategoryId(Integer categoryId);

    /**
     * 根据CategoryCode查询
     */
    Category getByCategoryCode(String categoryCode);

    /**
     * 根据CategoryCode查询
     */
    Integer updateByCategoryCode(Category bean, String categoryCode);

    /**
     * 根据CategoryCode删除
     */
    Integer deleteByCategoryCode(String categoryCode);


    void saveCategory(Category category);

    void delCategory(Integer categoryId);

    void changeSort(Integer pCategoryId, String categoryIds);

    List<Category> getCategoryList();
}