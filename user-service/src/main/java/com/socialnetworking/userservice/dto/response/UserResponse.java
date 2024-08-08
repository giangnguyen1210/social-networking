package com.socialnetworking.userservice.dto.response;

import com.socialnetworking.shared_service.dto.response.AvatarResponse;
import com.socialnetworking.userservice.model.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponse {
    private Long id;
    private String userId;
    private String name;
    private Date birthday;
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
    private AvatarResponse avatarData;
    private String bio;
    private Gender gender;
    private Date createdDate;
    private Date updatedDate;
    private boolean isFollowing;
}
