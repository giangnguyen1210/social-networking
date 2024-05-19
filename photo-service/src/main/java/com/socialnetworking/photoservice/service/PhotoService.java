package com.socialnetworking.photoservice.service;

import com.socialnetworking.photoservice.dto.response.BaseResponse;
import com.socialnetworking.photoservice.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {
    public BaseResponse addPhoto(String title, MultipartFile file) throws IOException;

    public Photo getPhoto(String id);
}
