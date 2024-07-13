package com.socialnetworking.photoservice.controller;

//import com.socialnetworking.photoservice.consumer.PhotoUploadConsumer;

import com.socialnetworking.photoservice.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@RequestMapping("/api/photo")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.upload-dir.avatar}")
    private String avatarDir;
    @GetMapping("/{photoId}")
    public ResponseEntity<?> getPhoto(@PathVariable Long photoId) {
        return new ResponseEntity<>(photoService.getPhotoById(photoId), HttpStatus.OK);
    }


    @GetMapping("/avatar/{filename}")
    public ResponseEntity<Resource> serveAvatar(@PathVariable String filename) {
        try {
            Path file = Paths.get(avatarDir).resolve(filename);
            System.out.println(file);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-avatar/{id}")
    public ResponseEntity<?> getAvatar(@PathVariable Long id) throws IOException {
        return new ResponseEntity<>(photoService.getAvatar(id), HttpStatus.OK);
    }
}
