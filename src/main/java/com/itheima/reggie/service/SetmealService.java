package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;


/**
 * 套餐(Setmeal)表服务接口
 *
 * @author makejava
 * @since 2022-12-21 18:37:26
 */
public interface SetmealService extends IService<Setmeal> {

    R<String> setmealSave(SetmealDto setmealDto);

    R<Page> setmeatpage(int page, int pageSize, String name);

    R<String> setmealDelete(List<Long> ids);

    R<String> setmealUpdate(List<Long> ids);

    R<String> setmealUpdate1(List<Long> ids);

    R<SetmealDto> getSetmealById(Long id);

    R<List<Setmeal>> listSetmeal(Setmeal setmeal);
}

