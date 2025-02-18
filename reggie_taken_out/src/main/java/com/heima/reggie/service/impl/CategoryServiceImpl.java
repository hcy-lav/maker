package com.heima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.reggie.bean.Category;
import com.heima.reggie.bean.Employee;
import com.heima.reggie.mapper.CategoryMapper;
import com.heima.reggie.mapper.EmployeeMapper;
import com.heima.reggie.service.CategoryService;
import com.heima.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public Page getCategoryByPage(int page, int pageSize) {
        Page page1 = new Page(page, pageSize);
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0);
        baseMapper.selectPage(page1, wrapper);
        return page1;
    }

    @Override
    public boolean deleteCategory(String ids) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("id", ids);
        int delete = baseMapper.delete(wrapper);
        if (delete == 0) {
            return false;
        }
        return true;
    }
}
