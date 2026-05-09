package com.pilipala.mappers;

import com.pilipala.entity.po.Category;
import com.pilipala.entity.query.CategoryQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Gqingci @Description: 分类信息的Mapper类
 * @date: 2025/10/01
 */
@Mapper
public interface CategoryMapper<T, P> extends BaseMapper {
  /** 根据CategoryId查询 */
  T selectByCategoryId(@Param("categoryId") Integer categoryId);

  /** 根据CategoryId更新 */
  Integer updateByCategoryId(@Param("bean") T t, @Param("categoryId") Integer categoryId);

  /** 根据CategoryId删除 */
  Integer deleteByCategoryId(@Param("categoryId") Integer categoryId);

  /** 根据CategoryCode查询 */
  T selectByCategoryCode(@Param("categoryCode") String categoryCode);

  /** 根据CategoryCode更新 */
  Integer updateByCategoryCode(@Param("bean") T t, @Param("categoryCode") String categoryCode);

  /** 根据CategoryCode删除 */
  Integer deleteByCategoryCode(@Param("categoryCode") String categoryCode);

  Integer selectMaxSort(@Param("pCategoryId") Integer pCategoryId);

  void deleteByParam(@Param("query") CategoryQuery query);

  void updateSortBatch(@Param("categoryList") List<Category> categoryList);
}
