package com.socialnetworking.userservice.dto.request;

import com.socialnetworking.userservice.model.Gender;
import com.socialnetworking.userservice.model.Role;
import com.socialnetworking.userservice.model.UserStatus;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class RegisterRequest {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String name;
    private Gender gender;
    private Date birthday;
    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "updated_at")
    private Date updatedDate;
    private Role role;
    private UserStatus status;
}
