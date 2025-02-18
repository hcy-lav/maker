package com.heima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.reggie.bean.Dish;
import com.heima.reggie.bean.dto.DishDto;
import com.heima.reggie.common.R;
import com.heima.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/dish")
@CrossOrigin
public class DishController {


    @Autowired
    private DishService dishService;

    //1.添加菜品
    @PostMapping()
    public R<String> addDish(@RequestBody DishDto dishDto, HttpServletRequest request) {
        dishService.addDish(dishDto,request);
        return R.success("添加成功");
    }

    //分页查询
    @GetMapping("page")
    public R<Page<Dish>> getDishPage( int page,int pageSize,String name){
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        if(name!=null){
            wrapper.like("name",name);
        }
        Page<Dish> dishpage = new Page<>(page, pageSize);
        dishService.page(dishpage,wrapper);
        return R.success(dishpage);
    }

    //查询详情
    @GetMapping("{id}")
    public R<DishDto> getDishById(@PathVariable String id) {
        DishDto dishDto=dishService.getDishInfoById(id);
        return R.success(dishDto);
    }


    //修改菜品信息
    @PutMapping()
    public R<String> updateDish(@RequestBody DishDto dishDto,HttpServletRequest request) {
        dishService.updateDish(dishDto,request);
        return R.success("修改成功");
    }

    //批量起售停售
    @PostMapping("status/{status}")
    public R<String> updateDishStatus(@PathVariable Integer status, @PathParam("id") String[] ids, HttpServletRequest request) {
        String updatee=(String)request.getSession().getAttribute("employee");
        for (String id : ids) {
            Dish byId = dishService.getById(id);
            byId.setStatus(status);
            byId.setUpdateTime(LocalDateTime.now());
            byId.setUpdateUser(updatee);
            dishService.updateById(byId);
        }
        return R.success("OK");
    }

    //批量删除
    @DeleteMapping()
    public R<String> deleteDish(@PathParam("id") String[] ids) {
        for (String id : ids) {
            Dish byId = dishService.getById(id);
            byId.setIsDeleted(1);
            dishService.updateById(byId);
        }
        return R.success("OK");
    }
}
