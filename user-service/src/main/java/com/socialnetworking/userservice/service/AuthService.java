package com.socialnetworking.userservice.service;

import com.socialnetworking.userservice.dto.request.LoginRequest;
import com.socialnetworking.userservice.dto.request.OTPVerificationRequest;
import com.socialnetworking.userservice.dto.response.AuthResponse;
import com.socialnetworking.userservice.dto.response.BaseResponse;
import com.socialnetworking.userservice.model.User;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    BaseResponse register(User request);

    BaseResponse sendEmailOTP(User request) throws NoSuchAlgorithmException, InvalidKeyException;

    BaseResponse verifyEmail(OTPVerificationRequest request);

}
