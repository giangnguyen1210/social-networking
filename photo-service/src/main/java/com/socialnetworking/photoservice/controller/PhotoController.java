package com.socialnetworking.photoservice.controller;

import com.socialnetworking.photoservice.dto.response.BaseResponse;
import com.socialnetworking.photoservice.model.Photo;
import com.socialnetworking.photoservice.service.impl.PhotoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    @Autowired
    private PhotoServiceImpl photoService;

    @PostMapping("/add")
    public ResponseEntity<?> addPhoto(@RequestParam("title") String title,
                                 @RequestParam("image") MultipartFile image) throws IOException {
        return new ResponseEntity<>(photoService.addPhoto(title, image), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Photo> getPhoto(@PathVariable String id) {
        Photo photo = photoService.getPhoto(id);
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//        return new ResponseEntity<byte[]>(photo.getImage().getData(), headers, HttpStatus.OK);
        return new ResponseEntity<Photo>(photo, HttpStatus.OK);
    }
}
