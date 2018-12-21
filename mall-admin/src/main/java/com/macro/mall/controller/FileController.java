package com.macro.mall.controller;

import com.alibaba.fastjson.JSON;
import com.macro.mall.dto.CommonResult;
import com.macro.mall.dto.OssPolicyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.apache.commons.lang.StringUtils.isEmpty;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {
    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload-folder}")
    private static String UPLOAD_FOLDER;

    @Value("${web.upload-path}")
    private static String UPLOAD_PTAH;

    @Value("${app.domain}")
    private static String APP_DOMAIN;

    // 检查配置
    static {
        logger.debug("UPLOAD_PTAH={}", UPLOAD_PTAH);
        logger.debug(UPLOAD_FOLDER);
        if (isEmpty(UPLOAD_FOLDER)) {
            UPLOAD_FOLDER = "/file/";
        }
        logger.debug(APP_DOMAIN);
        if (isEmpty(APP_DOMAIN)) {
            APP_DOMAIN = "http://localhost:8888";
        }
    }

    @PostMapping("/upload/single")
    public Object singleFileUpload(MultipartFile file) {
        logger.debug("传入的文件参数：{}", JSON.toJSONString(file, true));
        if (Objects.isNull(file) || file.isEmpty()) {
            logger.error("文件为空");
            return "文件为空，请重新上传";
        }

        try {
            byte[] bytes = file.getBytes();
            // todo: 使用uuid生成文件名称，在数据库表里添加文件名称字段
            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(UPLOAD_FOLDER));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            logger.debug("文件写入成功...");
            OssPolicyResult ossPolicyResult = new OssPolicyResult();
            ossPolicyResult.setDir("images");
            ossPolicyResult.setHost(APP_DOMAIN);
           /* OssCallbackResult ossCallbackResult = new OssCallbackResult();
            ossCallbackResult.setFilename(file.getName());
            ossCallbackResult.setMimeType(file.getContentType());
            ossCallbackResult.setSize(String.valueOf(file.getSize()));*/
            return new CommonResult().success(ossPolicyResult);
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }
}