package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理(Dish)表服务实现类
 *
 * @author makejava
 * @since 2022-12-21 18:30:51
 */
@Service("dishService")
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;



    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     * @return
     */
    @Override
    @Transactional  //开启事务支持（操作2张表）
    public R<String> saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜单表dish
        save(dishDto);
        //菜单id
        Long id = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream()
                .map((item) ->{
                    item.setDishId(id);
                    return item;
                }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
        return R.success("新增菜品成功");
    }

    /**
     * 查询所有菜品并分页
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    @Transactional
    public R<Page> dishPage(int page, int pageSize,String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getIsDeleted,0);
        //执行分页查询
        page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 更新dish和dish_flavor这两张表
     * @param dishDto
     * @return
     */
    @Override
    @Transactional
    public R<String> updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        updateById(dishDto);
        //更新dish_flavor表信息
        //1.清理当前菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //2.添加当前菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream()
                .map((item) ->{
                    item.setDishId(dishDto.getId());
                    return item;
                }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

        return R.success("菜品更新成功");
    }

    /**
     * 根据条件查询菜品信息
     * @param dish
     * @return
     */
    @Override
    public R<List<DishDto>> dishList(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dish.getName()), Dish::getName, dish.getName());
        queryWrapper.eq(null != dish.getCategoryId(), Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        List<Dish> dishs = list(queryWrapper);

        List<DishDto> dishDtos = dishs.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, item.getId());

            dishDto.setFlavors(dishFlavorService.list(wrapper));
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }

    /**
     * 停售
     * @param ids
     * @return
     */
    @Override
    public R<String> editStatus(List<Long> ids) {

        List<Dish> dishes = ids.stream()
                .map((item) -> {
                    Dish dish = getById(item);
                    dish.setStatus(0);
                    return dish;
                }).collect(Collectors.toList());
        updateBatchById(dishes);
        return R.success("停售成功");
    }

    /**
     * 启售
     * @param ids
     * @return
     */
    @Override
    public R<String> editStatus1(List<Long> ids) {
        List<Dish> dishes = ids.stream()
                .map((item) -> {
                    Dish dish = getById(item);
                    dish.setStatus(1);
                    return dish;
                }).collect(Collectors.toList());
        updateBatchById(dishes);
        return R.success("启售成功");
    }

    /**
     * 删除dish
     * @param ids
     * @return
     */
    @Override
    public R<String> deleteDish(List<Long> ids) {
        List<Dish> dishes = ids.stream()
                .map((item) -> {
                    Dish dish = getById(item);
                    dish.setIsDeleted(1);
                    return dish;
                }).collect(Collectors.toList());
        updateBatchById(dishes);
        return R.success("删除成功");
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */

    @Transactional
    @Override
    public R<DishDto> getByIdWithFlavor(Long id) {
        //查询菜品的基本信息，从dish表中查询
        Dish dish = getById(id);

        //Bean拷贝
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return R.success(dishDto);
    }
}

