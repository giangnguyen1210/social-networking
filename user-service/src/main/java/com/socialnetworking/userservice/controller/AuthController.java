package com.socialnetworking.userservice.controller;

import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.userservice.dto.request.LoginRequest;
import com.socialnetworking.userservice.dto.request.RegisterRequest;
import com.socialnetworking.userservice.dto.response.AuthResponse;
import com.socialnetworking.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService service;


    @PostMapping("/sign-in")
    public AuthResponse signIn(@RequestBody LoginRequest request){
        return new ResponseEntity<>(service.login(request), HttpStatus.OK).getBody();
    }

    @PostMapping("/sign-up")
    public BaseResponse signUp(@RequestBody RegisterRequest request){
        return new ResponseEntity<>(service.register(request), HttpStatus.OK).getBody();
    }

//    @PostMapping("/send-email-verify")
//    public ResponseEntity<BaseResponse> sendEmail(@RequestBody RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
//        return new ResponseEntity<>(service.sendEmailOTP(request), HttpStatus.OK);
//    }

//    @PostMapping("/activate-account")
//    public ResponseEntity<BaseResponse> activate(@RequestBody OTPVerificationRequest request){
//        return new ResponseEntity<>(service.verifyEmail(request), HttpStatus.OK);
//    }
}
