package com.taotao.controller;

import com.taotao.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PictureController {
    @Value("${path}")
    private String path;
    @Value("${prefix}")
    private String prefix;


    @RequestMapping("/pic/upload")
    @ResponseBody
    public String picUpload(MultipartFile uploadFile) {
        //接受

        try {
            String originalFilename = uploadFile.getOriginalFilename();
            //取扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //上传到图片服务器
            String uploadPath = upload(uploadFile, extName);
            Map<String, Object> result = new HashMap<>();
            result.put("error", 0);
            result.put("url", uploadPath);
            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", 1);
            result.put("message", "图片上传错误");
            return JsonUtils.objectToJson(result);
        }
    }

    private String upload(MultipartFile uploadFile, String extName) {

        String fileName = UUID.randomUUID().toString() + "." + extName;
        File file = new File(path + fileName);

        try (OutputStream outputStream = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(outputStream)
        ) {
            bos.write(uploadFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prefix + fileName;
    }
}
