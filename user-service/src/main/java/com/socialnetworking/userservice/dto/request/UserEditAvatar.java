package com.socialnetworking.userservice.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class UserEditAvatar {
    private Long id;
    private Long userId;
    private MultipartFile file;
}