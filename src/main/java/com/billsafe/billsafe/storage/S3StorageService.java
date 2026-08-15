package com.billsafe.billsafe.storage;

import com.billsafe.billsafe.storage.FileStorageException;
import com.billsafe.billsafe.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file, String folder) {
        String key = folder + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            return key;

        } catch (IOException e) {
            throw new FileStorageException(
                    "Failed to upload file to S3",
                    e
            );
        }
    }

    @Override
    public void delete(String filePath) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        s3Client.deleteObject(request);
    }

    @Override
    public Resource download(String filePath){
        try{
            InputStream inputStream=s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(filePath)
                            .build()
            );
            return new InputStreamResource(inputStream);
        }
        catch (Exception e){
            throw new FileStorageException(
                    "Failed to download file from S3",
                    e
            );
        }
    }
}
