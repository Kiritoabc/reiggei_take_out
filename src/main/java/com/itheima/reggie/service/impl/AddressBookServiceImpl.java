package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.config.BaseContext;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.mapper.AddressBookMapper;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    /**
     * 删除用户地址
     * @param ids
     * @return
     */
    @Override
    public R<String> delete(Long ids) {

        if (ids == null){
            return R.error("请求异常");
        }
        //需要判断该地址是否能删除

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId,ids).eq(AddressBook::getUserId, BaseContext.getCurrentId());
        AddressBook address = getOne(queryWrapper);
        address.setIsDeleted(1);
        updateById(address);
        return R.success("删除地址成功");

    }
}
