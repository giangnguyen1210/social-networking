package com.socialnetworking.postservice.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostRequest {
    private Long id;
    private String title;
    private Long userId;
    private List<MultipartFile> files;
}
