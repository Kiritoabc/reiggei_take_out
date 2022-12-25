package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 分类管理
 */

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * category的分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        return categoryService.categoryPage(page,pageSize);
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        return categoryService.deleteCategory(ids);
    }

    /**
     * 修改cayegory
     * @param category
     * @return
     */
    @PutMapping
    public R<String> edit(@RequestBody Category category){

        return categoryService.edit(category);
    }

    /**
     * category的类型
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List> list(Category category){
        return categoryService.categoryList(category);
    }
}
