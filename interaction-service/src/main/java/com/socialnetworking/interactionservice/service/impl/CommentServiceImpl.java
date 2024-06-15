package com.socialnetworking.interactionservice.service.impl;

import com.socialnetworking.interactionservice.dto.request.CommentRequest;
import com.socialnetworking.interactionservice.model.Comment;
import com.socialnetworking.interactionservice.producer.CommentEventProducer;
import com.socialnetworking.interactionservice.repository.CommentRepository;
import com.socialnetworking.interactionservice.service.CommentService;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository repository;
    @Autowired
    private CommentEventProducer commentEventProducer;
    @Override
    public BaseResponse saveComment(CommentRequest comment) {
        BaseResponse baseResponse = new BaseResponse();
        Comment com = new Comment();
        com.setContent(comment.getContent());
        com.setCreatedAt(LocalDateTime.now());
        com.setUserId(comment.getUserId());
        com.setPostId(comment.getPostId());
        com.setParentId(comment.getParentId());
        repository.save(com);
        baseResponse.setData(com);
        baseResponse.setErrorCode(HttpStatus.OK.name());
        commentEventProducer.sendComment(com);
        return baseResponse;
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return repository.findByPostId(postId);
    }

    public List<Comment> getRepliesByCommentId(Long commentId) {
        return repository.findByParentId(commentId);
    }
}
