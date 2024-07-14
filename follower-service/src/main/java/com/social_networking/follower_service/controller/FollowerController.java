package com.social_networking.follower_service.controller;

import com.social_networking.follower_service.dto.request.FollowerRequest;
import com.social_networking.follower_service.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/followers")
public class FollowerController {
    @Autowired
    private FollowerService followerService;
    @PostMapping("/create")
    public ResponseEntity<?> saveFollower(@RequestBody FollowerRequest request){
        return new ResponseEntity<>(followerService.saveFollow(request), HttpStatus.OK);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unFollow(@RequestBody FollowerRequest request){
        return new ResponseEntity<>(followerService.unFollow(request), HttpStatus.OK);
    }

}
