package com.heima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.reggie.bean.Category;
import com.heima.reggie.common.R;
import com.heima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //分页查询分类
    @GetMapping("/page")
    public R<Page<Category>> getCategory(int page, int pageSize) {
        //根据is_delete查询
        Page page1 = categoryService.getCategoryByPage(page, pageSize);
        return R.success(page1);
    }

    //根据id查询详细信息
    @GetMapping("/{id}")
    public R<Category> getCategory(@PathVariable int id) {
        Category category = categoryService.getById(id);
        return R.success(category);
    }

    //根据id删除分类:根据is_delete删除
    @DeleteMapping()
    public R<String> deleteCategory(String ids) {
        boolean res = categoryService.deleteCategory(ids);
        String msg = new String();
        if (res) {
            msg = "删除成功";
        } else {
            msg = "删除失败";
        }
        return R.success(msg);
    }

    //添加分类
    @PostMapping()
    public R<String> addCategory(HttpServletRequest request, @RequestBody Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        String empId = (String) request.getSession().getAttribute("employee");
        category.setCreateUser(empId);
        category.setUpdateUser(empId);
        boolean save = categoryService.save(category);
        if (save) {
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    //修改分类
    @PutMapping()
    public R<String> updateCategory(HttpServletRequest request, @RequestBody Category category) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("id", category.getId());
        category.setUpdateTime(LocalDateTime.now());
        String empId = (String) request.getSession().getAttribute("employee");
        category.setUpdateUser(empId);
        boolean update = categoryService.update(category, wrapper);
        if (update) {
            return R.success("修改失败");
        }
        return R.error("修改失败");
    }

    // 获取菜品分类列表
    @GetMapping("list")
    public R<List<Category>> getCategoryList() {
        List<Category> list = categoryService.list(null);
        return R.success(list);
    }




}
