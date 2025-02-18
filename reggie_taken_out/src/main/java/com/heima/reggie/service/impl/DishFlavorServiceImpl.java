package com.heima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.reggie.bean.Dish;
import com.heima.reggie.bean.DishFlavor;
import com.heima.reggie.mapper.DishFlavorMapper;
import com.heima.reggie.mapper.DishMapper;
import com.heima.reggie.service.DishFlavorService;
import com.heima.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl  extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
