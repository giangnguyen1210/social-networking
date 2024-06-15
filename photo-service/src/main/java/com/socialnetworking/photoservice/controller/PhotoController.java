package com.socialnetworking.photoservice.controller;

//import com.socialnetworking.photoservice.consumer.PhotoUploadConsumer;
import com.socialnetworking.photoservice.dto.request.PhotoRequest;
import com.socialnetworking.photoservice.service.PhotoService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {
    @Autowired
    private PhotoService photoService;


    @GetMapping("/{photoId}")
    public ResponseEntity<?> getPhoto(@PathVariable Long photoId) {
        return new ResponseEntity<>(photoService.getPhotoById(photoId), HttpStatus.OK);
    }
}
