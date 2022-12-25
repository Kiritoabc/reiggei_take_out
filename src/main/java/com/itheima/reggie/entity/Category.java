package com.itheima.reggie.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 菜品及套餐分类(Category)表实体类
 *
 * @author makejava
 * @since 2022-12-21 16:50:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("category")
public class Category  {
    //主键@TableId
    private Long id;

    //类型   1 菜品分类 2 套餐分类
    private Integer type;
    //分类名称
    private String name;
    //顺序
    private Integer sort;
    //创建时间
    @TableField(fill =  FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //创建人
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}

