package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.enums.AppHttpCodeEnum;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 员工信息(Employee)表服务实现类
 *
 * @author makejava
 * @since 2022-12-20 10:51:45
 */
@Service("employeeService")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    /**
     * 用户登录的实现类
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        //1.将页面提交的密码password进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面跳的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = getOne(queryWrapper);
        //3.如果没有查询到则返回登录失败结果
        if(Objects.isNull(emp)){
            //返回登录失败
            return R.error(AppHttpCodeEnum.LOGIN_ERROR.getMsg());
        }
        //4.密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            //返回密码错误
            return R.error(AppHttpCodeEnum.PASSWORD_ERROR.getMsg());
        }
        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }
        //6.登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @Override
    public R<String> logout(HttpServletRequest request) {
        //1.清理Session中的用户id
        request.getSession().removeAttribute("employee");
        //2.返回结果
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<String> saveEmployee(HttpServletRequest request, Employee employee) {

        //1.设置初始密码123456，需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //2.设置创建更新时间（获取系统当前时间）
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        //保存
        save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> EmployeePage(int page, int pageSize, String name) {

        //构造分页构造器
        Page pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 更新员工信息
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<String> updateEmployee(HttpServletRequest request, Employee employee) {

        //设置更新时间
//        employee.setUpdateTime(LocalDateTime.now());
        //设置更新人
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
        //更具用户Id更新数据
        updateById(employee);
        return R.success("更新成功");
    }


}

