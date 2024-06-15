package com.socialnetworking.interactionservice.controller;

import com.socialnetworking.interactionservice.dto.request.CommentRequest;
import com.socialnetworking.interactionservice.model.Comment;
import com.socialnetworking.interactionservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequest comment) {
        return new ResponseEntity<>(commentService.saveComment(comment), HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/replies/{commentId}")
    public List<Comment> getRepliesByCommentId(@PathVariable Long commentId) {
        return commentService.getRepliesByCommentId(commentId);
    }
}
