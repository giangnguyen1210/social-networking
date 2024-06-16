package com.socialnetworking.interactionservice.controller;

import com.socialnetworking.interactionservice.dto.request.CommentRequest;
import com.socialnetworking.interactionservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest comment) {
        return new ResponseEntity<>(commentService.saveComment(comment), HttpStatus.CREATED);
    }


    @PostMapping("/delete")
    public ResponseEntity<?> deleteCommentById(@RequestBody CommentRequest comment) {
        return new ResponseEntity<>(commentService.deleteCommentById(comment), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateComment(@RequestBody CommentRequest comment) {
        return new ResponseEntity<>(commentService.updateComment(comment), HttpStatus.OK);
    }

    @GetMapping("/get/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.getCommentsByPostId(postId), HttpStatus.OK);
    }

    @GetMapping("/replies/{commentId}")
    public ResponseEntity<?> getRepliesByCommentId(@PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.getRepliesByCommentId(commentId), HttpStatus.OK);
    }
}
