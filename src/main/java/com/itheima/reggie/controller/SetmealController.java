package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        return setmealService.setmealSave(setmealDto);
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        return setmealService.setmeatpage(page,pageSize,name);
    }

    /**
     * 套餐的删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        return setmealService.setmealDelete(ids);
    }

    /**
     * 根据传过来的ids停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> edit(@RequestParam List<Long> ids){
        log.info("ids ：{}" ,ids);
        return setmealService.setmealUpdate(ids);
    }

    /**
     * 根据传过来的ids启售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> editByids(@RequestParam List<Long> ids){
        return setmealService.setmealUpdate1(ids);
    }

    /**
     * 获取菜品信息根据id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        return setmealService.getSetmealById(id);
    }

    /**
     * 跟具条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        return setmealService.listSetmeal(setmeal);
    }
}
