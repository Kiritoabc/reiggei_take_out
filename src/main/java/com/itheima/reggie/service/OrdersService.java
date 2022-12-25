package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;


/**
 * 订单表(Orders)表服务接口
 *
 * @author makejava
 * @since 2022-12-23 11:41:22
 */
public interface OrdersService extends IService<Orders> {

    R<Page> ordersPage(int page, int pageSize, String number, String beginTime, String endTime);

    R<String> submit(Orders orders);

    R<Page<OrderDetail>> OrderPage(int page, int pageSize);
}

