package com.example.onboardingservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "s3")
public class S3FileStorageProperties {
    private String url;
    private String user;
    private String password;
    private Duration connectTimeout;
    private String documentBucket;
}
