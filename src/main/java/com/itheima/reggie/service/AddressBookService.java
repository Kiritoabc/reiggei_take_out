package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.AddressBook;

public interface AddressBookService extends IService<AddressBook> {

    R<String> delete(Long ids);
}
