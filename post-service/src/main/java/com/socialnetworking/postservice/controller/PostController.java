package com.socialnetworking.postservice.controller;

import com.socialnetworking.postservice.dto.request.PostRequest;
import com.socialnetworking.postservice.dto.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private RestTemplate restTemplate;

    private final String userServiceUrl = "localhost:9090"; // Địa chỉ của User Service
    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest) {
        ResponseEntity<UserRequest> userResponse = restTemplate.exchange(userServiceUrl + "/" + postRequest.getUserId(),
                HttpMethod.GET, null, UserRequest.class);

        if (userResponse.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>("Created post successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setUserId")
    public ResponseEntity<String> setUserIdForPost(@RequestBody Long userId) {
        return new ResponseEntity<>("Successfully set userId for posts", HttpStatus.OK);
    }
}
