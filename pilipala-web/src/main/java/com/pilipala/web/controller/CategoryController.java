package com.pilipala.web.controller;

import com.pilipala.web.annotation.GlobalInterceptor;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController extends ABaseController {
    @Resource
    private CategoryService categoryService;

    @RequestMapping("/loadAllCategory")
    @GlobalInterceptor
    public ResponseVO loadAllCategory() {
        return getSuccessResponseVo(categoryService.getCategoryList());
    }
}
