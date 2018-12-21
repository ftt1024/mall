package com.macro.mall.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

    @Value("${file.upload-folder}")
    private String mImagesPath;

    {
        if (isEmpty(mImagesPath)) {
            mImagesPath = "file:/file/";
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*String imagesPath = WebAppConfig.class.getClassLoader().getResource("").getPath();
        if (imagesPath.indexOf(".jar") > 0) {
            imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
        } else if (imagesPath.indexOf("classes") > 0) {
            imagesPath = "file:" + imagesPath.substring(0, imagesPath.indexOf("classes"));
        }
        imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/")) + "/images/";
        mImagesPath = imagesPath;*/
        LoggerFactory.getLogger(WebAppConfig.class).info("imagesPath=" + mImagesPath);
        registry.addResourceHandler("/images/**").addResourceLocations(mImagesPath); // 设置图片位置 http://localhost:8080/images/movie_ad.jpg
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}