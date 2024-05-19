package com.socialnetworking.postservice.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String userId;
    private List<String> photoUrls;
    // Getters và setters
}
