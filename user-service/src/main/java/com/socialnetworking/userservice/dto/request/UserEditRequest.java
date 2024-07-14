package com.socialnetworking.userservice.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class UserEditRequest {
    private Long id;
    private String userId;
    private String name;
    private String username;
    private String bio;
    private Date birthday;
    private Long genderId;
}
