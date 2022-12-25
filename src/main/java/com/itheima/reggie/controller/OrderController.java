package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private OrdersService ordersService;

    /**
     * orders分页和查询
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page>  page(int page, int pageSize, String number, String beginTime,String endTime){

        log.info("{}------,------{},--------{},------{},--------{}",page,pageSize,number,beginTime,endTime);

        return ordersService.ordersPage(page,pageSize,number,beginTime,endTime);
    }

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        return ordersService.submit(orders);
    }

    /**
     * 用户订单信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page<OrderDetail>> page(int page,int pageSize){
        return ordersService.OrderPage(page,pageSize);
    }
}
