package com.example.onboardingservice.service;

import com.example.onboardingservice.config.S3FileStorageProperties;
import com.example.onboardingservice.model.FileData;
import com.example.onboardingservice.model.FileMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MimeType;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.crt.AwsCrtHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.utils.AttributeMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class FileService {
    private static final int FIVE_MB = 5 * 1024 * 1024;
    private static final String MINIO_SDK_ATTRIBUTE_PREFIX = "x-amz-meta-attr-";

    private static final String ATTRIBUTE_PREFIX = "x-amz-meta-";
    private static final String FILENAME_HEADER = ATTRIBUTE_PREFIX + "filename";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";

    private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("application/octet-stream");

    private final S3FileStorageProperties properties;

    private S3Client client;

    private S3Client getClient() {
        if (client == null) {
            synchronized (this) {
                var credentials = AwsBasicCredentials.create(properties.getUser(), properties.getPassword());
                var httpClient = AwsCrtHttpClient.builder()
                        .connectionTimeout(properties.getConnectTimeout())
                        .buildWithDefaults(AttributeMap.builder().build());
                try {
                    client = S3Client.builder()
                            .endpointOverride(new URI(properties.getUrl()))
                            .region(Region.AWS_GLOBAL)
                            .credentialsProvider(StaticCredentialsProvider.create(credentials))
                            .serviceConfiguration(config -> config.pathStyleAccessEnabled(true))
                            .httpClient(httpClient)
                            .build();
                } catch (Exception e) {
                    log.error("Ошибка при создании клиента S3", e);
                }
            }
        }
        return client;
    }

    private Map<String, String> createMetadataFromFile(FileData file) {
        var metadata = new HashMap<String, String>();

        file.getMetadata().getAttributes().forEach((k, v) -> metadata.put(ATTRIBUTE_PREFIX + k, v));
        if (file.getMetadata().getFileName() != null) {
            metadata.put(FILENAME_HEADER, file.getMetadata().getFileName());
        }

        return metadata;
    }

    private FileMetadata extractMetadata(GetObjectResponse response, String key) {
        var metadata = new HashMap<String, String>();

        metadata.put(CONTENT_TYPE_HEADER, response.contentType());
        metadata.put(CONTENT_LENGTH_HEADER, response.contentLength().toString());
        response.metadata().forEach((attribute, value) -> metadata.put(ATTRIBUTE_PREFIX + attribute, value));

        return extractMetadata(metadata, key, false);
    }

    private FileMetadata extractMetadata(HeadObjectResponse response, String key) {
        var metadata = new HashMap<String, String>();

        metadata.put(CONTENT_TYPE_HEADER, response.contentType());
        metadata.put(CONTENT_LENGTH_HEADER, response.contentLength().toString());
        response.metadata().forEach((attribute, value) -> metadata.put(ATTRIBUTE_PREFIX + attribute, value));

        return extractMetadata(metadata, key, false);
    }

    /**
     * Извлечение заголовков из мета-данных S3 файла.
     * В интернетах говорят, что это специфичный Minio метод, по этому всё так хреново.
     * TODO: Надо прибраться будет
     *
     * @param metadata    Заголовки файла
     * @param documentKey Ключ по которому находится файл (на случай, если имя файла явно не указано)
     * @param keyToLower  костыль, сравнивать ли всё в lowercase и переводить ли в него
     *                    (флаг нужен из-за багованности S3-библиотеки, в некоторых случаях она возвращает заголовки в странном кейсе)
     * @return Метаданные файла
     */
    private FileMetadata extractMetadata(Map<String, String> metadata, String documentKey, boolean keyToLower) {
        Function<String, String> transform = (s) -> keyToLower ? s.toLowerCase() : s;
        var mimeType = new AtomicReference<>(DEFAULT_CONTENT_TYPE);
        var filename = new AtomicReference<>(documentKey);
        var size = new AtomicReference<>(-1L);
        var attributes = new HashMap<String, String>();
        metadata.forEach((k, v) -> {
            String transformedKey = transform.apply(k);
            if (transformedKey.equals(FILENAME_HEADER)) {
                filename.set(v);
            } else if (k.equalsIgnoreCase(CONTENT_TYPE_HEADER)) {
                mimeType.set(MimeType.valueOf(v));
            } else if (k.equalsIgnoreCase(CONTENT_LENGTH_HEADER)) {
                size.set(Long.parseLong(v));
            } else if (transformedKey.startsWith(ATTRIBUTE_PREFIX)) {
                // для обратной совместимости
                var beginIndex = transformedKey.startsWith(MINIO_SDK_ATTRIBUTE_PREFIX)
                        ? MINIO_SDK_ATTRIBUTE_PREFIX.length()
                        : ATTRIBUTE_PREFIX.length();
                attributes.put(transformedKey.substring(beginIndex), v);
            }
        });

        return new FileMetadata(mimeType.get(), filename.get(), attributes, size.get());
    }

    // костыль для ZipInputStream, который может вернуть available() = 0
    // при том, что из него еще можно вычитать
    private long readFromChannelToByteBuffer(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        long readBytesSum = 0;
        int readBytes;

        while ((readBytes = channel.read(buffer)) > 0) {
            readBytesSum += readBytes;
        }

        return readBytesSum;
    }

    private void putFileFromBytesBuffer(ByteBuffer byteBuffer, String bucket, FileData file, Map<String, String> metadata) {
        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(file.getKey())
                .contentType(file.getMetadata().getContentType().toString())
                .metadata(metadata)
                .build();
        // в ByteArrayInputStream для предотвращения копирования массива
        var stream = new ByteArrayInputStream(byteBuffer.array());
        getClient().putObject(putObjectRequest, RequestBody.fromInputStream(stream, byteBuffer.limit()));
    }

    private CompletedPart putPartOfMultipart(String uploadId, int partNumber, String bucket, String key, ByteBuffer byteBuffer) {
        var partRequest = UploadPartRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .partNumber(partNumber)
                .build();
        // в ByteArrayInputStream для предотвращения копирования массива
        var stream = new ByteArrayInputStream(byteBuffer.array());
        var partResponse = getClient().uploadPart(partRequest, RequestBody.fromInputStream(stream, byteBuffer.limit()));

        return CompletedPart.builder().partNumber(partNumber).eTag(partResponse.eTag()).build();
    }

    private long putFileWithoutSize(FileData file, String bucket, Map<String, String> metadata) {
        var key = file.getKey();

        long fileSize = 0;
        var byteBuffer = ByteBuffer.allocate(FIVE_MB);
        try {
            var inputChannel = Channels.newChannel(file.getInputStream());
            var readBytes = readFromChannelToByteBuffer(inputChannel, byteBuffer);
            log.debug("Размер предварительного буфера загрузки файла {} [{}] - {} байт", file.getMetadata().getFileName(), file.getKey(), readBytes);

            if (readBytes < FIVE_MB) {
                byteBuffer.flip();
                putFileFromBytesBuffer(byteBuffer, bucket, file, metadata);

                return readBytes;
            } else {
                log.debug("Загрузка файла {} [{}] партициями по 5 МБ", file.getMetadata().getFileName(), file.getKey());
                var createMultipartUpload = CreateMultipartUploadRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .metadata(metadata)
                        .contentType(file.getMetadata().getContentType().toString())
                        .build();

                var uploadId = getClient().createMultipartUpload(createMultipartUpload).uploadId();
                var parts = new LinkedList<CompletedPart>();
                var partNumber = 1;

                do {
                    byteBuffer.flip();

                    var part = putPartOfMultipart(uploadId, partNumber++, bucket, key, byteBuffer);
                    parts.add(part);

                    byteBuffer.clear();
                    fileSize += readBytes;
                } while ((readBytes = readFromChannelToByteBuffer(inputChannel, byteBuffer)) > 0);

                CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .uploadId(uploadId)
                        .multipartUpload(builder -> builder.parts(parts))
                        .build();

                getClient().completeMultipartUpload(completeRequest);

                return fileSize;
            }
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка при сохранении файла [%s] в бакет %s".formatted(file.getKey(), bucket), exception);
        }
    }

    public FileData getFile(String key) {
        ResponseInputStream<GetObjectResponse> response;
        try {
            log.trace("Получение файла [{}] из бакета {}", key, properties.getDocumentBucket());
            response = getClient().getObject(builder -> builder.bucket(properties.getDocumentBucket()).key(key));
            log.info("Получен файл [{}] из бакета {}, headers: {}", key, properties.getDocumentBucket(), response.response().metadata());
        } catch (NoSuchKeyException responseException) {
            throw new RuntimeException("Файл [%s] не найден в хранилище".formatted(key), responseException);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения файла [%s] из бакета %s".formatted(key, properties.getDocumentBucket()), e);
        }

        var fileMetadata = extractMetadata(response.response(), key);
        return new FileData(key, response, fileMetadata);
    }

    public FileMetadata getMetadata(String key) {
        HeadObjectResponse response;
        try {
            log.trace("Получение метаданных файла [{}]", key);
            response = getClient().headObject(builder -> builder.bucket(properties.getDocumentBucket()).key(key));
            log.info("Получены метаданные [{}], headers: {}", key, response.metadata());
        } catch (NoSuchKeyException responseException) {
            throw new RuntimeException("Файл [%s] не найден в хранилище".formatted(key), responseException);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения файла [%s] из бакета %s".formatted(key, properties.getDocumentBucket()), e);
        }

        return extractMetadata(response, key);
    }

    public long putFile(FileData file) {
        log.trace("Сохранение файла {} [{}] в бакет {}", file.getMetadata().getFileName(), file.getKey(), properties.getDocumentBucket());

        var metadata = createMetadataFromFile(file);

        long fileSize;
        if (file.getMetadata().getFileSize() == null) {
            log.debug("Сохранение файла неизвестного размера {} [{}] в бакет {}", file.getMetadata().getFileName(), file.getKey(), properties.getDocumentBucket());
            fileSize = putFileWithoutSize(file, properties.getDocumentBucket(), metadata);
        } else {
            log.debug("Сохранение файла {} [{}] размером ({} байт) в бакет {}", file.getMetadata().getFileName(), file.getKey(), file.getMetadata().getFileSize(), properties.getDocumentBucket());
            try {
                var inputStream = file.getInputStream();
                var requestBody = RequestBody.fromInputStream(inputStream, file.getMetadata().getFileSize());
                var putObjectRequest = PutObjectRequest.builder()
                        .bucket(properties.getDocumentBucket())
                        .key(file.getKey())
                        .contentType(file.getMetadata().getContentType().toString())
                        .metadata(metadata)
                        .build();
                getClient().putObject(putObjectRequest, requestBody);
                fileSize = file.getMetadata().getFileSize();
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при сохранении файла [%s] в бакет %s".formatted(file.getKey(), properties.getDocumentBucket()), e);
            }
        }
        log.info("Файл {} [{}] успешно сохранен в бакет {}", file.getMetadata().getFileName(), file.getKey(), properties.getDocumentBucket());

        return fileSize;
    }

    public void deleteFile(String key) {
        log.trace("Удаление файла [{}]", key);
        try {
            getClient().deleteObject(builder -> builder.bucket(properties.getDocumentBucket()).key(key));
            log.info("Файл [{}] успешно удален", key);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении файла [%s] из хранилище".formatted(key), e);
        }
    }

    public boolean checkExists(String key) {
        log.trace("Проверка существования файла [{}]", key);
        try {
            getClient().headObject(builder -> builder.bucket(properties.getDocumentBucket()).key(key));

            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверки наличия файла [%s] из хранилище".formatted(key), e);
        }
    }

    /**
     * Создание нового бакета.
     * Если такой уже есть, то ничего не происходит.
     */
    public void createBucket(String name) {
        try {
            if (!bucketExists(name)) {
                var client = getClient();
                var waiter = client.waiter();

                client.createBucket(builder -> builder.bucket(name));
                waiter.waitUntilBucketExists(builder -> builder.bucket(name));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании бакета [%s] в хранилище".formatted(name), e);
        }
    }

    /**
     * Проверка наличия бакета в хранилище
     */
    public boolean bucketExists(String name) {
        try {
            getClient().headBucket(builder -> builder.bucket(name));

            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }
}
