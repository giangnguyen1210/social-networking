package com.socialnetworking.interactionservice.service;

import com.socialnetworking.interactionservice.dto.request.CommentRequest;
import com.socialnetworking.shared_service.dto.response.BaseResponse;

public interface CommentService {
    BaseResponse saveComment(CommentRequest comment);

    BaseResponse deleteCommentById(CommentRequest commentRequest);

    BaseResponse updateComment(CommentRequest commentRequest);

    BaseResponse getCommentsByPostId(Long postId);
    BaseResponse getRepliesByCommentId(Long commentId);
}
