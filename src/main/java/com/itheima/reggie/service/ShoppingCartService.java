package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;

import java.util.List;


/**
 * 购物车(ShoppingCart)表服务接口
 *
 * @author makejava
 * @since 2022-12-24 19:43:46
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    R<ShoppingCart> addShoppingCart(ShoppingCart shoppingCart);

    R<List<ShoppingCart>> listShoppingCart();

    R<String> clean();
}

