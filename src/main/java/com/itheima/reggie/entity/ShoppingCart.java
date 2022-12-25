package com.itheima.reggie.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 购物车(ShoppingCart)表实体类
 *
 * @author makejava
 * @since 2022-12-24 19:43:45
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("shopping_cart")
public class ShoppingCart  {
    //主键@TableId
    private Long id;

    //名称
    private String name;
    //图片
    private String image;
    //主键
    private Long userId;
    //菜品id
    private Long dishId;
    //套餐id
    private Long setmealId;
    //口味
    private String dishFlavor;
    //数量
    private Integer number;
    //金额
    private BigDecimal amount;
    //创建时间
    private LocalDateTime createTime;



}

