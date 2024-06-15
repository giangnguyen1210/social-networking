package com.socialnetworking.postservice.dto.request;

import lombok.Data;

@Data
public class User {
    private String userId;
    private String name;
    private String username;
    private String email;
    private String password;
}
