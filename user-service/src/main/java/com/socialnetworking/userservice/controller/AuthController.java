package com.socialnetworking.userservice.controller;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.userservice.dto.request.LoginRequest;
import com.socialnetworking.userservice.dto.request.OTPVerificationRequest;
import com.socialnetworking.userservice.dto.request.RegisterRequest;
import com.socialnetworking.userservice.dto.response.AuthResponse;
import com.socialnetworking.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService service;

    @PostMapping("/token")
    public String getToken(@RequestBody LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(authentication.isAuthenticated()){
            return service.generateToken(request.getUsername());
        }else{
            throw new RuntimeException("Invalid Access");
        }
    }

    @PostMapping("/sign-in")
    public AuthResponse signIn(@RequestBody LoginRequest request){
        return new ResponseEntity<>(service.login(request), HttpStatus.OK).getBody();
    }

    @PostMapping("/sign-up")
    public BaseResponse signUp(@RequestBody RegisterRequest request){
        return new ResponseEntity<>(service.register(request), HttpStatus.OK).getBody();
    }

    @PostMapping("/send-email-verify")
    public ResponseEntity<BaseResponse> sendEmail(@RequestBody RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        return new ResponseEntity<>(service.sendEmailOTP(request), HttpStatus.OK);
    }

    @PostMapping("/activate-account")
    public ResponseEntity<BaseResponse> activate(@RequestBody OTPVerificationRequest request){
        return new ResponseEntity<>(service.verifyEmail(request), HttpStatus.OK);
    }
}
