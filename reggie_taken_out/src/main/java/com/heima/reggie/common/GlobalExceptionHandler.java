package com.heima.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理
@ControllerAdvice(annotations ={ RestController.class, Controller.class  })//对那些类进行检查
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    //处理这种SQLIntegrityConstraintViolationException异常的方法
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")) {
            String[] s = ex.getMessage().split(" ");
            String msg = s[2];
            return R.error(msg+"重复注册");
        }
        return R.error("未知错误");
    }
}
