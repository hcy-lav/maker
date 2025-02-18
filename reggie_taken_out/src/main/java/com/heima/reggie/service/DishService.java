package com.heima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.reggie.bean.Category;
import com.heima.reggie.bean.Dish;
import com.heima.reggie.bean.dto.DishDto;

import javax.servlet.http.HttpServletRequest;

public interface DishService extends IService<Dish> {
    //添加菜品
    void addDish(DishDto dishDto, HttpServletRequest request);

    //查询详情包括口味
    DishDto getDishInfoById(String id);

    //修改菜品
    void updateDish(DishDto dishDto, HttpServletRequest request);
}
