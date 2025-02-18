package com.heima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.reggie.bean.Category;
import com.heima.reggie.bean.Employee;

public interface CategoryService extends IService<Category> {
    //根据is_delete查询
    Page getCategoryByPage(int page, int pageSize);

    //删除分类
    boolean deleteCategory(String ids);
}
