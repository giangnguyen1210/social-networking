package com.socialnetworking.userservice.service;

import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.userservice.dto.request.LoginRequest;
import com.socialnetworking.userservice.dto.request.RegisterRequest;
import com.socialnetworking.userservice.dto.request.UserRequest;
import com.socialnetworking.userservice.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    BaseResponse register(RegisterRequest request);
    String generateToken(String username, Long id);

//    BaseResponse sendEmailOTP(RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeyException;

//    BaseResponse verifyEmail(OTPVerificationRequest request);

    BaseResponse resetPassword(UserRequest request);
}
