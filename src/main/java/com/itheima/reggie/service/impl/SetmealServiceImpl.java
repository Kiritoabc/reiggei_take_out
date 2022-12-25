package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 套餐(Setmeal)表服务实现类
 *
 * @author makejava
 * @since 2022-12-21 18:37:27
 */
@Service("setmealService")
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @Transactional
    @Override
    public R<String> setmealSave(SetmealDto setmealDto) {
        //保存套餐的基本信息
        save(setmealDto);

        //保存套餐和菜品的关联信息，操作setmeal_dish
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream()
                .map((item) ->{
                    item.setSetmealId(setmealDto.getId());
                    return item;
                }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> setmeatpage(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，更具更新时间僵尸排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream()
                    .map((item)->{
                        SetmealDto setmealDto = new SetmealDto();
                        BeanUtils.copyProperties(item,setmealDto);
                        //分类id
                        Long categoryId = item.getCategoryId();
                        //跟具分类id查询分类对象
                        Category category = categoryService.getById(categoryId);
                        if(category!=null){
                            String categoryName = category.getName();
                            setmealDto.setCategoryName(categoryName);
                        }
                        return setmealDto;
                    }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 套餐的删除同事删除套餐和菜品的关联数据
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public R<String> setmealDelete(List<Long> ids) {

        //查询套餐状态，确认是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0 ){
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据-----setmeal
        removeByIds(ids);
        //删除关系表中的数据----------setmeal_dish
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);

        return R.success("删除成功");
    }

    /**
     * 根据传过来的ids停售
     * @param ids
     * @return
     */
    @Override
    public R<String> setmealUpdate(List<Long> ids) {

        //构造查询器
        List<Setmeal> setmeals = listByIds(ids);
        setmeals =setmeals.stream()
                    .map((item) ->{
                        item.setStatus(0);
                        return item;
                    }).collect(Collectors.toList());

        updateBatchById(setmeals);
        return R.success("停售成功");
    }

    /**
     * 根据传过来的ids启售
     * @param ids
     * @return
     */
    @Override
    public R<String> setmealUpdate1(List<Long> ids) {
        //构造查询器
        List<Setmeal> setmeals = listByIds(ids);
        setmeals =setmeals.stream()
                .map((item) ->{
                    item.setStatus(1);
                    return item;
                }).collect(Collectors.toList());

        updateBatchById(setmeals);
        return R.success("启售成功");
    }

    /**
     * 获取菜品信息根据id
     * @param id
     * @return
     */
    @Override
    public R<SetmealDto> getSetmealById(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        //查询菜品的基本信息
        Setmeal setmeal = getById(id);
        //实现Bean拷贝
        BeanUtils.copyProperties(setmeal,setmealDto);
        //查询categoryName
        Long categoryId = setmeal.getCategoryId();
        Category category = categoryService.getById(categoryId);
        setmealDto.setCategoryName(category.getName());
        //查询setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);

        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        //返回
        return R.success(setmealDto);
    }

    /**
     * 跟具条件查询套餐数据
     * @param setmeal
     * @return
     */
    @Override
    public R<List<Setmeal>> listSetmeal(Setmeal setmeal) {

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId() !=null, Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus() !=null,Setmeal::getStatus,setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = list(lambdaQueryWrapper);
        return R.success(list);
    }
}

