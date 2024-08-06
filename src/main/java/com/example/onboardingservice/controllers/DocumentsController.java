package com.example.onboardingservice.controllers;

import com.example.onboardingservice.model.documents.DocumentDto;
import com.example.onboardingservice.service.DocumentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
public class DocumentsController {

    private final DocumentsService documentsService;

    @GetMapping
    public ResponseEntity<List<DocumentDto>> getDocuments() {
        return ResponseEntity.ok(documentsService.getDocuments());
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable UUID documentId) {
        return ResponseEntity.ok(documentsService.getDocument(documentId));
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadDocuments(@RequestParam("documentId") List<UUID> documentIds) {
        var fileData = documentsService.download(documentIds);
        var contentDisposition = ContentDisposition.attachment()
                .filename(fileData.getMetadata().getFileName(), StandardCharsets.UTF_8)
                .build();
        var headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(fileData.getInputStream()));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<DocumentDto>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(documentsService.uploadDocuments(files));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<Void> updateDocuments(@RequestBody DocumentDto documentDto, @PathVariable UUID documentId) {
        documentsService.updateDocumentFileName(documentDto, documentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDocuments(@RequestParam("documentId") List<UUID> documentIds) {
        documentsService.deleteDocument(documentIds);
        return ResponseEntity.ok().build();
    }
}
