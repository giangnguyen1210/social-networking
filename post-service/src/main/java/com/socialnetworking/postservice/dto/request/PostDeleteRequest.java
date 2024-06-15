package com.socialnetworking.postservice.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostDeleteRequest {
    private Long id;
    private Long userId;
    // Getters và setters
}