package com.social_networking.follower_service.dto.request;

import lombok.Data;

@Data
public class FollowerRequest {
    private Long id;
    private Long followerId;
    private Long followingId;
}
