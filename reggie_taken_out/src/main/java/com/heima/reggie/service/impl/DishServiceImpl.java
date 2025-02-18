package com.heima.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.reggie.bean.Category;
import com.heima.reggie.bean.Dish;
import com.heima.reggie.bean.DishFlavor;
import com.heima.reggie.bean.dto.DishDto;
import com.heima.reggie.mapper.CategoryMapper;
import com.heima.reggie.mapper.DishMapper;
import com.heima.reggie.service.CategoryService;
import com.heima.reggie.service.DishFlavorService;
import com.heima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional
    public void addDish(DishDto dishDto, HttpServletRequest request) {
        //保存菜品的基本信息到菜品表dish,因为dishDto是dish子类所以直接可以保存
        dishDto.setCreateTime(LocalDateTime.now());
        dishDto.setUpdateTime(LocalDateTime.now());
        String updatee=(String)request.getSession().getAttribute("employee");
        dishDto.setUpdateUser(updatee);
        dishDto.setCreateUser(updatee);
        this.save(dishDto);
        String dishId = dishDto.getId();//菜品id
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setUpdateUser(updatee);
            item.setCreateUser(updatee);
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getDishInfoById(String id) {
        DishDto dishDto = new DishDto();
        Dish dish = baseMapper.selectById(id);
        //记住要用这个工具类
        BeanUtils.copyProperties(dish,dishDto);
        //查询口味列表
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        //查询种类名称
        Category byId = categoryService.getById(dish.getCategoryId());
        dishDto.setCategoryName(byId.getName());
        dishDto.setCopies(1);
        return dishDto;
    }

    @Override
    public void updateDish(DishDto dishDto,HttpServletRequest request) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        String id = dish.getId();
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        dish.setUpdateTime(LocalDateTime.now());
        baseMapper.update(dish,queryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        String updatee=(String)request.getSession().getAttribute("employee");
        flavors = flavors.stream().map((item) -> {
            item.setUpdateUser(updatee);
            item.setUpdateTime(LocalDateTime.now());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.updateBatchById(flavors);
    }
}
