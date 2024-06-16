package com.socialnetworking.interactionservice.controller;

import com.socialnetworking.interactionservice.dto.request.LikeRequest;
import com.socialnetworking.interactionservice.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @PostMapping("/status")
    public ResponseEntity<Map<String, Boolean>> isPostLikedByUser(@RequestBody LikeRequest likeRequest) {
        boolean isLiked = likeService.isPostLikedByUser(likeRequest.getPostId(), likeRequest.getUserId());
        Map<String, Boolean> response = new HashMap<>();
        response.put("isLiked", isLiked);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createLike(@RequestBody LikeRequest likeRequest) {
        return new ResponseEntity<>(likeService.createLike(likeRequest), HttpStatus.OK);
    }

    @PostMapping("/get-by-post-id")
    public ResponseEntity<?> getLikeByPostId(@RequestBody LikeRequest likeRequest) {
        return new ResponseEntity<>(likeService.getLikeByPostId(likeRequest), HttpStatus.OK);
    }

    @PostMapping("/get-by-user-id")
    public ResponseEntity<?> getLikeByUserId(@RequestBody LikeRequest likeRequest) {
        return new ResponseEntity<>(likeService.getLikeByUserId(likeRequest), HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeLike(@RequestBody LikeRequest likeRequest) {
        return new ResponseEntity<>(likeService.removeLike(likeRequest), HttpStatus.OK);
    }
}
