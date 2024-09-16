package com.example.task_service_jwt.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private final String uploadDirectory = "src/main/resources/upload_covers";

    public String saveImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, fileName);
        Files.copy(image.getInputStream(), filePath);
        return "/upload_covers/" + fileName;
    }
}

