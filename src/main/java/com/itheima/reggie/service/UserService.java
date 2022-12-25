package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;

import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * 用户信息(User)表服务接口
 *
 * @author makejava
 * @since 2022-12-24 12:13:51
 */
public interface UserService extends IService<User> {

    R<String> sendMag(HttpSession session, User user);

    R<User> login(Map user, HttpSession session);

    R<String> loginout(HttpSession session);
}

