package com.example.onboardingservice.service;

import com.example.onboardingservice.model.FileData;
import com.example.onboardingservice.model.documents.DocumentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DocumentsService {
    List<DocumentDto> getDocuments();

    DocumentDto getDocument(UUID documentId);

    FileData download(List<UUID> documentIds);

    List<DocumentDto> uploadDocuments(List<MultipartFile> documents);

    void updateDocumentFileName(DocumentDto updatedDocument, UUID documentId);

    void deleteDocument(List<UUID> documentIds);
}
