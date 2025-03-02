package com.global.hr.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // إنشاء اسم فريد للملف
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetLocation = this.rootLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage folder!");
        }
    }
}
