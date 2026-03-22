package com.food.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Value("${app.upload.directory:./uploads}")
    private String uploadDirectory;

    @Value("${app.qr.code.directory:./qrcodes}")
    private String qrCodeDirectory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDirectory + "/");
        
        registry.addResourceHandler("/qrcodes/**")
                .addResourceLocations("file:" + qrCodeDirectory + "/");
    }
}
