package com.example.onboardingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.MimeType;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class FileMetadata {
    private MimeType contentType;
    private String fileName;
    private Map<String, String> attributes;
    private Long fileSize;
}
