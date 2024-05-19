package com.socialnetworking.photoservice.service;

import com.socialnetworking.photoservice.dto.VideoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {
     String addVideo(String title, MultipartFile file) throws IOException;
     VideoDTO getVideo(String id) throws IOException;
}
