package com.example.onboardingservice.config;

import com.example.onboardingservice.service.FileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfiguration {

    @Bean
    public FileService fileService(S3FileStorageProperties properties) {
        var service = new FileService(properties);
        service.createBucket(properties.getDocumentBucket());

        return service;
    }
}
