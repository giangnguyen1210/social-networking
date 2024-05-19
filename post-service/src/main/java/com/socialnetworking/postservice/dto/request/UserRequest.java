package com.socialnetworking.postservice.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String userId;
    private String username;
}
