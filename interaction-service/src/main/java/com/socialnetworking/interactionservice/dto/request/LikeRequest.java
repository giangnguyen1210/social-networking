package com.socialnetworking.interactionservice.dto.request;

import lombok.Data;

@Data
public class LikeRequest {
    private Long id;
    private Long postId;
    private Long userId;
}