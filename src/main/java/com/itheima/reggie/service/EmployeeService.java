package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;


/**
 * 员工信息(Employee)表服务接口
 *
 * @author makejava
 * @since 2022-12-20 10:51:44
 */
public interface EmployeeService extends IService<Employee> {

    R<Employee> login(HttpServletRequest request, Employee employee);

    R<String> logout(HttpServletRequest request);

    R<String> saveEmployee(HttpServletRequest request, Employee employee);

    R<Page> EmployeePage(int page, int pageSize, String name);

    R<String> updateEmployee(HttpServletRequest request, Employee employee);
}

