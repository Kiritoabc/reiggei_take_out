package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;

import java.util.List;


/**
 * 菜品及套餐分类(Category)表服务接口
 *
 * @author makejava
 * @since 2022-12-21 16:50:40
 */
public interface CategoryService extends IService<Category> {

    R<Page> categoryPage(int page, int pageSize);

    R<String> deleteCategory(Long id);

    R<String> edit(Category category);

    R<List> categoryList(Category type);
}

