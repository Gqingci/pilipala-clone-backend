package com.pilipala.service.impl;

import com.pilipala.component.RedisComponent;
import com.pilipala.entity.query.CategoryQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.Category;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.CategoryMapper;
import com.pilipala.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import com.pilipala.entity.enums.PageSize;
import com.pilipala.service.CategoryService;

/**
 * @author Gqingci @Description: 分类信息对应的ServiceImpl
 * @date: 2025/10/01
 */
@Slf4j
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper<Category, CategoryQuery> categoryMapper;

    @Resource
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Category> findListByParam(CategoryQuery query) {
        List<Category> categoryList = this.categoryMapper.selectList(query);
        if (query.getConvert2Tree() != null && query.getConvert2Tree()) {
            categoryList = convertLine2Tree(categoryList, Constants.ZERO);
        }
        return categoryList;
    }

    private List<Category> convertLine2Tree(List<Category> dataList, Integer pid) {
        List<Category> children = new ArrayList<>();
        for (Category category : dataList) {
            if (category.getCategoryId() != null
                    && category.getPCategoryId() != null
                    && category.getPCategoryId().equals(pid)) {
                category.setChildren(convertLine2Tree(dataList, category.getCategoryId()));
                children.add(category);
            }
        }
        return children;
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(CategoryQuery query) {
        return this.categoryMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<Category> findListByPage(CategoryQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize =
                query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<Category> list = this.findListByParam(query);
        PaginationResultVO<Category> result =
                new PaginationResultVO(
                        count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Category bean) {
        return this.categoryMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Category> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.categoryMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(Category category) {
        return this.categoryMapper.insertOrUpdate(category);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Category> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.categoryMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据CategoryId查询
     */
    @Override
    public Category getByCategoryId(Integer categoryId) {
        return this.categoryMapper.selectByCategoryId(categoryId);
    }

    /**
     * 根据CategoryId更新
     */
    @Override
    public Integer updateByCategoryId(Category bean, Integer categoryId) {
        return this.categoryMapper.updateByCategoryId(bean, categoryId);
    }

    /**
     * 根据CategoryId删除
     */
    @Override
    public Integer deleteByCategoryId(Integer categoryId) {
        return this.categoryMapper.deleteByCategoryId(categoryId);
    }

    /**
     * 根据CategoryCode查询
     */
    @Override
    public Category getByCategoryCode(String categoryCode) {
        return this.categoryMapper.selectByCategoryCode(categoryCode);
    }

    /**
     * 根据CategoryCode更新
     */
    @Override
    public Integer updateByCategoryCode(Category bean, String categoryCode) {
        return this.categoryMapper.updateByCategoryCode(bean, categoryCode);
    }

    /**
     * 根据CategoryCode删除
     */
    @Override
    public Integer deleteByCategoryCode(String categoryCode) {
        return this.categoryMapper.deleteByCategoryCode(categoryCode);
    }

    @Override
    public void saveCategory(Category bean) {
        Category dbBean = this.categoryMapper.selectByCategoryCode(bean.getCategoryCode());
        if ((bean.getCategoryId() == null && dbBean != null)
                || (bean.getCategoryId() != null
                && dbBean != null
                && !bean.getCategoryId().equals(dbBean.getCategoryId()))) {
            throw new BusinessException("分类编号已存在");
        }

        if (bean.getCategoryId() == null) {
            Integer maxSort = this.categoryMapper.selectMaxSort(bean.getPCategoryId());
            bean.setSort(maxSort + 1);
            this.categoryMapper.insert(bean);
        } else {
            this.categoryMapper.updateByCategoryId(bean, bean.getCategoryId());
        }

        save2Redis();
    }

    @Override
    public void delCategory(Integer categoryId) {
        // TODO 查询该分类下是否有视频

        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setCategoryIdOrPCategoryId(categoryId);
        categoryMapper.deleteByParam(categoryQuery);

        save2Redis();
    }

    @Override
    public void changeSort(Integer pCategoryId, String categoryIds) {
        String[] categoryIdArray = categoryIds.split(",");
        List<Category> categoryList = new ArrayList<>();
        Integer sort = 0;
        for (String categoryId : categoryIdArray) {
            Category category = new Category();
            category.setCategoryId(Integer.parseInt(categoryId));
            category.setPCategoryId(pCategoryId);
            category.setSort(++sort);
            categoryList.add(category);
        }
        this.categoryMapper.updateSortBatch(categoryList);

        save2Redis();
    }

    @Override
    public List<Category> getCategoryList() {
        List<Category> categoryList = redisComponent.getCategoryList();
        if (categoryList.isEmpty()) {
            // 刷新缓存
            save2Redis();
        }
        return redisComponent.getCategoryList();
    }

    private void save2Redis() {
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setOrderBy("sort asc");
        categoryQuery.setConvert2Tree(true);
        List<Category> categoryList = findListByParam(categoryQuery);
        redisComponent.saveCategoryList(categoryList);
    }
}
