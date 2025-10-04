package com.pilipala.admin.controller;

import com.pilipala.entity.po.Category;
import com.pilipala.entity.query.CategoryQuery;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController extends ABaseController {

    @Resource
    private CategoryService categoryService;

    @RequestMapping("/loadCategory")
    public ResponseVO loadCategory(CategoryQuery categoryQuery) {
        categoryQuery.setOrderBy("sort asc");
        categoryQuery.setConvert2Tree(true);
        List<Category> categoryList = categoryService.findListByParam(categoryQuery);
        log.info("List:{}", categoryList);
        return getSuccessResponseVo(categoryList);
    }

    @RequestMapping("/saveCategory")
    public ResponseVO saveCategory(@NotNull Integer pCategoryId,
                                   Integer categoryId,
                                   @NotEmpty String categoryCode,
                                   @NotEmpty String categoryName,
                                   String icon,
                                   String background) {
        Category category = new Category();
        category.setPCategoryId(pCategoryId);
        category.setCategoryId(categoryId);
        category.setCategoryCode(categoryCode);
        category.setCategoryName(categoryName);
        category.setIcon(icon);
        category.setBackground(background);

        categoryService.saveCategory(category);

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/delCategory")
    public ResponseVO delCategory(@NotNull Integer categoryId) {
        categoryService.delCategory(categoryId);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/changeSort")
    public ResponseVO changeSort(@NotNull Integer pCategoryId, @NotNull String categoryIds) {
        categoryService.changeSort(pCategoryId, categoryIds);
        return getSuccessResponseVo(null);
    }


}
