package com.socialnetworking.userservice.dto.request;

import com.socialnetworking.shared_service.dto.response.base.AuditModel;
import com.socialnetworking.userservice.model.Gender;
import com.socialnetworking.userservice.model.Role;
import com.socialnetworking.userservice.model.UserStatus;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequest extends AuditModel {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
    private Gender gender;
    private Date birthday;
    private Role role;
    private UserStatus status;
}
