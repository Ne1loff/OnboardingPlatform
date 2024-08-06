package com.example.onboardingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FileData {
    private String key;
    private InputStream inputStream;
    private FileMetadata metadata;
}
