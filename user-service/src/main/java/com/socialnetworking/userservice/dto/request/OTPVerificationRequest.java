package com.socialnetworking.userservice.dto.request;

import lombok.Data;

@Data
public class OTPVerificationRequest {
    private String username;
    private String email;
    private String otp;
    private Long statusId;
}
