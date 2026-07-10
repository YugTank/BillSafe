package com.billsafe.billsafe.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService{

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @Override
    public String upload(MultipartFile file, String folder) {
        try{
            Path uploadPath= Paths.get(uploadDir).resolve(folder);

            if(Files.notExists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            String originalFileName= file.getOriginalFilename();

            String extenstion="";
            if(originalFileName!=null && originalFileName.contains(".")){
                extenstion=originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String storedFileName= UUID.randomUUID().toString()+extenstion;
            Path destination=uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), destination);
            return destination.toString();
        }
        catch (IOException e){
            throw new FileStorageException("Failed to store file",e);
        }
    }

    @Override
    public void delete(String filePath) {
        try{
            Files.deleteIfExists(Paths.get(filePath));
        }
        catch (IOException e){
            throw new FileStorageException("Failed to delete file",e);
        }
    }
}
