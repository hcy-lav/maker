package com.heima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.reggie.bean.Employee;
import com.heima.reggie.common.R;
import com.heima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/employee")
@CrossOrigin
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ServletContext servletContext;

    //员工登录功能
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", employee.getUsername());
        Employee em = employeeService.getOne(wrapper);
        //判断用户存在
        if (em == null) {
            return R.error("用户不存在");
        }
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //判断密码是否错误
        if (!em.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        if (em.getStatus() == 0) {
            return R.error("用户被禁用");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", em.getId());
        return R.success(em);
    }

    //员工退出登录
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    //分页查询所有员工
    @GetMapping("/page")
    public R<Page> page(Long page, Long pageSize, String name) {
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("username", name);
        }
        wrapper.orderByDesc("update_time");
        Page<Employee> page1 = new Page<>(page, pageSize);
        employeeService.page(page1, wrapper);
        return R.success(page1);
    }

    //修改员工状态
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        String updatee=(String)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(updatee);
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    //添加员工
    //13091704659  330326200411135413
    @PostMapping()
    public R<String> add(HttpServletRequest request, @RequestBody Employee employee) {
        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获得当前登录用户的id
        String empId = (String) request.getSession().getAttribute("employee");
        System.out.println(empId);
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    //根据id查询员工
     @GetMapping("/{id}")
        public R<Employee> getById(@PathVariable String id){
         System.out.println(id);
            Employee employee = employeeService.getById(id);
            if(employee != null){
                return R.success(employee);
            }
            return R.error("没有查询到对应员工信息");
        }

}

