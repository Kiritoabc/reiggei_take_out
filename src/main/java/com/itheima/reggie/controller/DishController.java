package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        return dishService.saveWithFlavor(dishDto);
    }

    /**
     * 查询所有菜品并分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        return dishService.dishPage(page,pageSize,name);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        return dishService.getByIdWithFlavor(id);
    }

    /**
     * 更新dish和dish_flavor这两张表
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        return dishService.updateWithFlavor(dishDto);
    }

    /**
     * 根据条件查询菜品信息
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        return dishService.dishList(dish);
    }

    /**
     * 停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> editStatus0(@RequestParam List<Long> ids){

        return dishService.editStatus(ids);
    }

    /**
     * 启售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> editStatus1(@RequestParam List<Long> ids){
        return dishService.editStatus1(ids);
    }

    /**
     * 删除dish
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        return dishService.deleteDish(ids);
    }
}
