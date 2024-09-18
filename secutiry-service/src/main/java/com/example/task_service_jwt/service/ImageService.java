package com.example.task_service_jwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class ImageService {

    private final String uploadDirectory = "src/main/resources/upload_covers";

    @Value("${app.upload.path}")
    String uploadPath;

    public String saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, fileName);
        Files.copy(file.getInputStream(), filePath);

//        String savePath = saveNewBookImage(file);
//        Book bookToUpdate = bookRepository.findBookBySlug(slug);
//        bookToUpdate.setImage(savePath);
//        bookRepository.save(bookToUpdate);

        return "/upload_covers/" + fileName;
    }


//    public String saveNewBookImage(MultipartFile file) throws IOException {
//
//        String resourceURI = null;
//
//        if(!file.isEmpty()) {
//            if(!new File(uploadPath).exists()) {
//                Files.createDirectories(Paths.get(uploadPath));
//                Logger.getLogger(this.getClass().getSimpleName()).info("created image folder int " + uploadPath);
//            }
//
//            String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
//            Path path = Paths.get(uploadPath, fileName);
//            resourceURI = "/book-covers/" + fileName;
//            file.transferTo(path);
//            Logger.getLogger(this.getClass().getSimpleName()).info(fileName + " uploaded!");
//        }
//
//        return resourceURI;
//    }
}

