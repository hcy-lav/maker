package com.heima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.reggie.bean.Category;
import com.heima.reggie.bean.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
