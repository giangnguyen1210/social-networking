package com.socialnetworking.userservice.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String userId;
    private String username;
    private String password;
}
