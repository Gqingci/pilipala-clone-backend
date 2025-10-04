package com.pilipala.web.controller;

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

    @RequestMapping("/loadAllCategory")
    public ResponseVO loadAllCategory() {
        return getSuccessResponseVo(categoryService.getCategoryList());
    }
}
