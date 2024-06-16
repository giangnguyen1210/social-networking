package com.socialnetworking.interactionservice.service.impl;

import com.socialnetworking.interactionservice.dto.request.CommentRequest;
import com.socialnetworking.interactionservice.model.Comment;
import com.socialnetworking.interactionservice.producer.CommentEventProducer;
import com.socialnetworking.interactionservice.repository.CommentRepository;
import com.socialnetworking.interactionservice.service.CommentService;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.shared_service.dto.response.CommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository repository;
    @Autowired
    private CommentEventProducer commentEventProducer;
    @Override
    public BaseResponse saveComment(CommentRequest commentRequest) {
        BaseResponse baseResponse = new BaseResponse();
        if(commentRequest.getContent()!=null && commentRequest.getPostId()!=null && commentRequest.getUserId()!=null){
            Long finalParentId = null;
            if (commentRequest.getParentId() != null) {
                Optional<Comment> parentCommentOpt = repository.findByIdAndIsDeletedFalse(commentRequest.getParentId());
                if (parentCommentOpt.isEmpty()) {
                    baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
                    baseResponse.setErrorDesc("Parent comment does not exist");
                    return baseResponse;
                }
                Comment parentComment = parentCommentOpt.get();
                // If the parent comment has a parentId, use the top-level parent's ID
                finalParentId = (parentComment.getParentId() != null) ? parentComment.getParentId() : commentRequest.getParentId();
            }
            // create comment
            Comment com = new Comment();
            com.setContent(commentRequest.getContent());
            com.setCreatedAt(LocalDateTime.now());
            com.setCreatedBy(commentRequest.getUserId());
            com.setUserId(commentRequest.getUserId());
            com.setPostId(commentRequest.getPostId());
            com.setParentId(finalParentId);
            repository.save(com);
            // Create and send response
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setContent(com.getContent());
            commentResponse.setCreatedAt(LocalDateTime.now());
            commentResponse.setUserId(com.getUserId());
            commentResponse.setPostId(com.getPostId());
            commentResponse.setParentId(com.getParentId());
            commentEventProducer.sendComment(commentResponse);
            // return data response
            baseResponse.setData(com);
            baseResponse.setErrorCode(HttpStatus.OK.name());
        }else{
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("You have to give enough fields");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse deleteCommentById(CommentRequest commentRequest) {
        BaseResponse baseResponse = new BaseResponse();
        Comment comment = repository.getCommentByIdAndIsDeletedFalse(commentRequest.getId());
        List<Comment> comments = repository.findByParentIdAndIsDeletedFalse(commentRequest.getId());
        if(comment==null){
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("No record found");
        }else{
            comment.setIsDeleted(true);
            repository.save(comment);
            if(comments!=null){
                for(Comment com: comments){
                    com.setIsDeleted(true);
                    repository.save(com);
                }
            }

            // Create and send response
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setId(comment.getId());
            commentResponse.setUpdatedAt(LocalDateTime.now());
            commentResponse.setUpdatedBy(comment.getUpdatedBy());
            commentEventProducer.sendDeleteComment(commentResponse);
            baseResponse.setData(comment);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Delete success");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse updateComment(CommentRequest commentRequest) {
        BaseResponse baseResponse = new BaseResponse();
        Comment comment = repository.getCommentByIdAndIsDeletedFalse(commentRequest.getId());
        if(comment==null){
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("No record found");
        }else{
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setUpdatedBy(comment.getCreatedBy());
            comment.setContent(commentRequest.getContent());
            repository.save(comment);
            // Create and send response
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setContent(comment.getContent());
            commentResponse.setId(comment.getId());
            commentResponse.setUpdatedAt(LocalDateTime.now());
            commentResponse.setUpdatedBy(comment.getUpdatedBy());
            commentEventProducer.sendUpdateComment(commentResponse);
            baseResponse.setData(comment);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Update success");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse getCommentsByPostId(Long postId) {
        BaseResponse baseResponse = new BaseResponse();
        List<Comment> comments = repository.getCommentsByPostIdAndIsDeletedFalse(postId);

        if(comments==null){
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("No record found");
        }else{
            baseResponse.setData(comments);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Success to get post"+ postId);
        }
        return baseResponse;
    }

    public BaseResponse getRepliesByCommentId(Long commentId) {
        BaseResponse baseResponse = new BaseResponse();
        List<Comment> comments = repository.findByParentIdAndIsDeletedFalse(commentId);

        if(comments==null){
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("No record found");
        }else{
            baseResponse.setData(comments);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Success to get post"+ commentId);
        }
        return baseResponse;
    }
}
