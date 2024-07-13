package com.socialnetworking.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowerRequest {
    private Long userId;
    private Long followingId;
}
