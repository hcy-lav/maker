package com.heima.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.reggie.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
