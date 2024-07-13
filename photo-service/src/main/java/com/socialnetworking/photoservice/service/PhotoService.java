package com.socialnetworking.photoservice.service;

import com.socialnetworking.photoservice.dto.response.BaseResponse;
import com.socialnetworking.photoservice.model.Photo;

import java.io.IOException;
import java.util.Optional;

public interface PhotoService {

     BaseResponse getAvatar(Long userId) throws IOException;

     //    String handlePostMessage(PostResponse postResponse);
     Optional<Photo> getPhotoById(Long photoId);


}
