package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 给用户发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        return userService.sendMag(session,user);
    }

    /**
     * 移动端的用户登录
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user,HttpSession session){
        return userService.login(user,session);
    }

    /**
     * 移动端的用户退出登录
     * @param session
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpSession session){
        return userService.loginout(session);
    }
}
