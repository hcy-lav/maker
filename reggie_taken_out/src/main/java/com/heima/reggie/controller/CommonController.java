package com.heima.reggie.controller;


import com.heima.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/common")
public class CommonController {


    @Value("${reggie.path}")
    private String BASEPATH;


    //上传图片
    @PostMapping("upload")
    public R<String> upload( MultipartFile file) {
        //file是临时文件
        //创建一个目录对象
        File file1 = new File(BASEPATH);
        //判断是否存在，不存在则创建
        if (!file1.exists()) {
            file1.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(BASEPATH + file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(file.getOriginalFilename());
    }

    //下载图片
    @GetMapping("download")
    public void download(HttpServletResponse response, String filename) {
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(BASEPATH + filename));
            //输出流，输出到浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            //表示返回类型
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] buffer = new byte[1024];
            //等于-1表示读完，用byte数组表示读入的东西
            while ((len = fileInputStream.read(buffer)) != -1) {
                //将所有都读到页面上
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
