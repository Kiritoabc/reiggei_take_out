package com.itheima.reggie.controller;


import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件的上传和下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String bathPath;


    /**
     * 文件上传方法
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定的位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String uuid = UUID.randomUUID().toString();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //创建一个目录对象
        File  dir = new File(bathPath);
        if(!dir.exists()){
            //目录不存在则创建
            dir.mkdirs();
        }
        String fileName = uuid + suffix;
        try {
            file.transferTo(new File(bathPath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，读取文件内容
            FileInputStream fileInputStream = new FileInputStream(bathPath+name);
            //输出流，将文件写回浏览器，在浏览器中展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            //设置返回的类型（图片）
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while( (len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
