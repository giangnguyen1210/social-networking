package com.socialnetworking.interactionservice.service.impl;

import com.socialnetworking.interactionservice.dto.request.LikeRequest;
import com.socialnetworking.interactionservice.model.Like;
import com.socialnetworking.interactionservice.producer.LikeEventProducer;
import com.socialnetworking.interactionservice.repository.LikeRepository;
import com.socialnetworking.interactionservice.service.LikeService;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.shared_service.dto.response.LikeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeEventProducer likeEventProducer;

    @Override
    public BaseResponse createLike(LikeRequest likeRequest) {
        BaseResponse baseResponse = new BaseResponse();
        if(likeRequest.getPostId()!=null&& likeRequest.getUserId()!=null){
            Like existingLike = likeRepository.findByPostIdAndUserId(likeRequest.getPostId(), likeRequest.getUserId());
            if (existingLike!=null) {
                baseResponse.setErrorCode(HttpStatus.CONFLICT.name());
                baseResponse.setErrorDesc("Like already exists");
                return baseResponse;
            }
            Like like = new Like();
            like.setPostId(likeRequest.getPostId());
            like.setUserId(likeRequest.getUserId());
            like.setCreatedAt(LocalDateTime.now());
            like.setCreatedBy(likeRequest.getUserId());
            likeRepository.save(like);
            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setUserId(like.getUserId());
            likeResponse.setPostId(like.getPostId());
            likeEventProducer.sendLike(likeResponse);
            baseResponse.setData(like);
            baseResponse.setErrorCode(HttpStatus.CREATED.name());
            baseResponse.setErrorDesc("like thành công");
        }else{
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("No record found");
        }
        return baseResponse;
    }

    public BaseResponse removeLike(LikeRequest likeRequest) {
        BaseResponse baseResponse = new BaseResponse();
        if (likeRequest.getPostId() != null && likeRequest.getUserId() != null) {
            // Check if the like exists
            Like existingLike = likeRepository.findByPostIdAndUserId(likeRequest.getPostId(), likeRequest.getUserId());
            if (existingLike ==null) {
                baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
                baseResponse.setErrorDesc("Like not found");
                return baseResponse;
            }

            // Remove the like
            likeRepository.delete(existingLike);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Like removed successfully");
        } else {
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("PostId and UserId are required");
        }
        return baseResponse;
    }
    @Override
    public Boolean isPostLikedByUser(Long postId, Long userId) {
        Like like = likeRepository.findByPostIdAndUserId(postId, userId);
        return like!=null;
    }

    @Override
    public BaseResponse getLikeByPostId(Long postId) {
        BaseResponse baseResponse = new BaseResponse();
//        Long postId = likeRequest.getPostId();
        if(postId!=null) {
            List<Like> likes = likeRepository.findLikesByPostId(postId);
            if (likes.size()==0) {
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("No like");
                baseResponse.setTotalRecords(0);
            }else{
                baseResponse.setData(likes);
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Get likes successful");
                baseResponse.setTotalRecords(likes.size());
            }
        }else{
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("No post found");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse getLikeByUserId(LikeRequest likeRequest) {
        BaseResponse baseResponse = new BaseResponse();
        Long userId = likeRequest.getUserId();
        if(userId!=null) {
            List<Like> likes = likeRepository.findLikesByUserId(userId);
            if (likes.size()==0) {
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("No like");
                baseResponse.setTotalRecords(0);
            }else{
                baseResponse.setData(likes);
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Get likes successful");
                baseResponse.setTotalRecords(likes.size());
            }
        }else{
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("No user found");
        }
        return baseResponse;
    }
}
