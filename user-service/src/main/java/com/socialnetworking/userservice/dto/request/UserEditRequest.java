package com.socialnetworking.userservice.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class UserEditRequest {
    private Long id;
    private String userId;
    private String name;
    private String username;
    private String bio;
    private Date birthday;
    private Integer genderId;
//    private MultipartFile avatar;
}
