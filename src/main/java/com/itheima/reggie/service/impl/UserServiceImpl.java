package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户信息(User)表服务实现类
 *
 * @author makejava
 * @since 2022-12-24 12:13:52
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    /**
     * 给用户发送验证码
     *
     * @param session
     * @param user
     * @return
     */
    @Override
    public R<String> sendMag(HttpSession session, User user) {
        //获取手机号
        String phone = user.getPhone();
        //生成随机的4位验证码
        if(StringUtils.isNotBlank(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
        //调用阿里云提供的短信服务API完成发送短信
            log.info("code:{}",code);
          //  SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            //需要将验证码存起来
            session.setAttribute(phone,code);

            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端的用户登录
     * @param user
     * @param session
     * @return
     */
    @Override
    public R<User> login(Map user, HttpSession session) {

        //获取手机号
        String phone = user.get("phone").toString();

        //获取验证码
        String code = user.get("code").toString();
        //从Session中获取保存的验证码
        Object codeInnSession = session.getAttribute(phone);
        //进行验证码的比对（页面提交 的验证码和Session中保存的验证码比对）
        if(codeInnSession != null &&codeInnSession.equals(code)){
            //弱国能够比对成功，说明登录成功
            //判断当前手机号对应的用户是否为新用户，
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User one = getOne(queryWrapper);
            if(one == null){
                //如果是新用户，就自动完成注册
                one = new User();
                one.setPhone(phone);
                one.setStatus(1);
                save(one);
            }
            session.setAttribute("user",one.getId());
            return R.success(one);
        }
        return R.error("登录失败");
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getPhone,phone);
//        User one = getOne(queryWrapper);
//        if(one == null){
//            //如果是新用户，就自动完成注册
//            one = new User();
//            one.setPhone(phone);
//            one.setStatus(1);
//            save(one);
//        }
//        session.setAttribute("user",one.getId());
//        return R.success(one);
    }

    /**
     * 移动端的用户退出登录
     * @param session
     * @return
     */
    @Override
    public R<String> loginout(HttpSession session) {
        session.removeAttribute("user");
        return R.success("推出成功");
    }
}

