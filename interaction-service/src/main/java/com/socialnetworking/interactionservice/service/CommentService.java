package com.socialnetworking.interactionservice.service;

import com.socialnetworking.interactionservice.dto.request.CommentRequest;
import com.socialnetworking.interactionservice.model.Comment;
import com.socialnetworking.shared_service.dto.response.BaseResponse;

import java.util.List;

public interface CommentService {
    BaseResponse saveComment(CommentRequest comment);

    List<Comment> getCommentsByPostId(Long postId);
    List<Comment> getRepliesByCommentId(Long commentId);
}
