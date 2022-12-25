package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;


/**
 * 菜品管理(Dish)表服务接口
 *
 * @author makejava
 * @since 2022-12-21 18:30:51
 */
public interface DishService extends IService<Dish> {

     R<DishDto> getByIdWithFlavor(Long id);

    R<String> saveWithFlavor(DishDto dishDto);

    R<Page> dishPage(int page, int pageSize,String name);

    R<String> updateWithFlavor(DishDto dishDto);

    R<List<DishDto>> dishList(Dish dish);

    R<String> editStatus(List<Long> ids);

    R<String> editStatus1(List<Long> ids);

    R<String> deleteDish(List<Long> ids);
}

