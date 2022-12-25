package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜品及套餐分类(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-12-21 16:50:40
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * category的分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<Page> categoryPage(int page, int pageSize) {
        //分页构造器
        Page categoryInfo  = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //查询
        page(categoryInfo,queryWrapper);
        return R.success(categoryInfo);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @Override
    public R<String> deleteCategory(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count > 0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常‘
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1 > 0){
            throw  new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除分类
        removeById(id);
        return R.success("删除成功");
    }

    /**
     * 修改category
     * @param category
     * @return
     */
    @Override
    public R<String> edit(Category category) {
        updateById(category);
        return R.success("修改成功");
    }

    /**
     * category的类型
     * @param category
     * @return
     */
    @Override
    public R<List> categoryList(Category category) {

        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = list(queryWrapper);
        return R.success(list);
    }
}

