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
    @GetMapping("/status/{userId}/{postId}")
    public ResponseEntity<Map<String, Boolean>> isPostLikedByUser(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId ) {
        boolean isLiked = likeService.isPostLikedByUser(postId, userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isLiked", isLiked);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createLike(@RequestBody LikeRequest likeRequest) {
        return new ResponseEntity<>(likeService.createLike(likeRequest), HttpStatus.OK);
    }

    @GetMapping("/get-by-post-id/{id}")
    public ResponseEntity<?> getLikeByPostId(@PathVariable Long id) {
        return new ResponseEntity<>(likeService.getLikeByPostId(id), HttpStatus.OK);
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
