package com.food.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${app.upload.directory:./uploads}")
    private String uploadDirectory;

    public String uploadFile(MultipartFile file, String subDirectory) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        
        String newFilename = UUID.randomUUID().toString() + extension;
        
        Path uploadPath = Paths.get(uploadDirectory, subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);
        
        return "/uploads/" + subDirectory + "/" + newFilename;
    }

    public void deleteFile(String fileUrl) throws IOException {
        String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        Path filePath = Paths.get(uploadDirectory, filename);
        
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
