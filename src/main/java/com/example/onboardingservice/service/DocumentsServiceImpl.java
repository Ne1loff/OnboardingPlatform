package com.example.onboardingservice.service;

import com.example.onboardingservice.jooq.Tables;
import com.example.onboardingservice.jooq.tables.daos.OnboardingDocumentsDao;
import com.example.onboardingservice.jooq.tables.pojos.OnboardingDocuments;
import com.example.onboardingservice.model.FileData;
import com.example.onboardingservice.model.FileMetadata;
import com.example.onboardingservice.model.documents.DocumentDto;
import com.example.onboardingservice.service.helpers.ChannelWriter;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentsServiceImpl implements DocumentsService {

    private final FileService fileService;
    private final OnboardingDocumentsDao documentsDao;

    private final ChannelWriter channelWriter = ChannelWriter.builder()
            .setCloseTargetOnCompletion(false)
            .build();

    @Override
    public List<DocumentDto> getDocuments() {
        return documentsDao.findAll().stream()
                .map(it -> DocumentDto.of(it.getKey(), it.getFilename()))
                .toList();
    }

    @Override
    public DocumentDto getDocument(UUID documentId) {
        return documentsDao.fetchOptionalByKey(documentId)
                .map(it -> DocumentDto.of(it.getKey(), it.getFilename()))
                .orElseThrow(() -> new NotFoundException("Не удалось найти указанный файл"));
    }

    @Override
    public FileData download(List<UUID> documentIds) {
        var documents = documentsDao.fetch(Tables.ONBOARDING_DOCUMENTS.KEY, documentIds);
        if (documents.size() != documentIds.size()) {
            throw new NotFoundException("Не удалось найти указанные файлы");
        }

        var documentsFiles = documents.stream()
                .map(it -> new DocumentFileHolder(it, getFileFromStorageProvider(it.getKey())))
                .toList();

        try {
            return documents.size() == 1 ? prepareFile(documentsFiles.getFirst()) : prepareArchive(documentsFiles);
        } catch (IOException exception) {
            log.error("Error downloading documents", exception);
            throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public List<DocumentDto> uploadDocuments(List<MultipartFile> documents) {
        var documentsFiles = new LinkedList<FileData>();
        var documentsPojos = documents.stream()
                .map(this::fileDataFromMultipartFile)
                .peek(documentsFiles::add)
                .map(it -> new OnboardingDocuments(UUID.fromString(it.getKey()), it.getMetadata().getFileName()))
                .toList();

        documentsDao.insert(documentsPojos);
        documentsFiles.forEach(fileService::putFile);

        return documentsPojos.stream()
                .map(it -> DocumentDto.of(it.getKey(), it.getFilename()))
                .toList();
    }

    @Override
    @Transactional
    public void updateDocumentFileName(DocumentDto updatedDocument, UUID documentId) {
        if (!updatedDocument.id().equals(documentId)) {
            throw new BadRequestException("Идентификаторы документов не совпадают");
        }

        documentsDao.update(new OnboardingDocuments(updatedDocument.id(), updatedDocument.filename()));
    }

    @Override
    @Transactional
    public void deleteDocument(List<UUID> documentIds) {
        var documents = documentsDao.fetch(Tables.ONBOARDING_DOCUMENTS.KEY, documentIds);
        if (documents.size() != documentIds.size()) {
            throw new NotFoundException("Не удалось найти указанные файлы");
        }

        documentsDao.deleteById(documentIds);
        documents.forEach(it -> fileService.deleteFile(it.getKey().toString()));
    }

    private FileData fileDataFromMultipartFile(MultipartFile multipartFile) {
        try {
            var metadata = new FileMetadata(
                    MimeTypeUtils.APPLICATION_OCTET_STREAM,
                    multipartFile.getOriginalFilename(),
                    Collections.emptyMap(),
                    null);
            return FileData.builder()
                    .key(UUID.randomUUID().toString())
                    .metadata(metadata)
                    .inputStream(multipartFile.getInputStream())
                    .build();
        } catch (IOException exception) {
            log.error("Error uploading document", exception);
            throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private FileData prepareFile(DocumentFileHolder documentFile) throws IOException {
        var documentStream = new BufferedInputStream(documentFile.fileDataSupplier().get().getInputStream());
        var metadata = new FileMetadata(MimeTypeUtils.APPLICATION_OCTET_STREAM, documentFile.document().getFilename(), Collections.emptyMap(), null);

        return FileData.builder()
                .inputStream(documentStream)
                .metadata(metadata)
                .build();
    }


    private FileData prepareArchive(List<DocumentFileHolder> documentsFiles) throws IOException {
        var zipFilePath = Files.createTempFile("zip", UUID.randomUUID().toString());
        var fileChannel = FileChannel.open(zipFilePath, StandardOpenOption.WRITE);

        try (var archive = new ZipArchiveOutputStream(fileChannel)) {
            for (var documentFile : documentsFiles) {
                archive.putArchiveEntry(new ZipArchiveEntry(documentFile.document.getFilename()));
                var archiveChannel = Channels.newChannel(archive);
                var documentStream = new BufferedInputStream(documentFile.fileDataSupplier().get().getInputStream());

                channelWriter.write(archiveChannel, Channels.newChannel(documentStream));
                archive.closeArchiveEntry();
            }
        }

        var archiveChannel = FileChannel.open(zipFilePath, StandardOpenOption.READ, StandardOpenOption.DELETE_ON_CLOSE);
        var metadata = new FileMetadata(MimeTypeUtils.APPLICATION_OCTET_STREAM, "documents.zip", Collections.emptyMap(), null);
        return FileData.builder()
                .inputStream(new BufferedInputStream(Channels.newInputStream(archiveChannel)))
                .metadata(metadata)
                .build();
    }

    private Supplier<FileData> getFileFromStorageProvider(final UUID id) {
        return () -> fileService.getFile(id.toString());
    }

    private record DocumentFileHolder(OnboardingDocuments document, Supplier<FileData> fileDataSupplier) {
    }
}
