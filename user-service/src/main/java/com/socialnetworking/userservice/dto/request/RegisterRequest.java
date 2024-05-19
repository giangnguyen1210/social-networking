package com.socialnetworking.userservice.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String name;
    private Integer gender;
    private Date birthday;
}
