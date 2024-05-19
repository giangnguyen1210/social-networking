package com.socialnetworking.userservice.controller;

import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.response.BaseResponse;
import com.socialnetworking.userservice.model.User;
import com.socialnetworking.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/get-info")
    public BaseResponse getInfo(@RequestBody User request){
        return new ResponseEntity<>(userService.getInfo(request.getUsername()), HttpStatus.OK).getBody();
    }


    @PostMapping("/update-info")
    public BaseResponse updateInfo(@RequestBody UserEditRequest request){
        return new ResponseEntity<>(userService.update(request), HttpStatus.OK).getBody();
    }
}
