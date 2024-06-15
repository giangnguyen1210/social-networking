package com.socialnetworking.postservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUploadConfig {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Bean
    public String uploadDir() {
        return uploadDir;
    }
}

