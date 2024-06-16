package com.socialnetworking.interactionservice.service;

import com.socialnetworking.interactionservice.dto.request.LikeRequest;
import com.socialnetworking.shared_service.dto.response.BaseResponse;

public interface LikeService {
    BaseResponse createLike(LikeRequest likeRequest);
    BaseResponse removeLike(LikeRequest likeRequest);

    Boolean isPostLikedByUser(Long postId, Long userId);

    BaseResponse getLikeByPostId(LikeRequest likeRequest);

    BaseResponse getLikeByUserId(LikeRequest likeRequest);
}
