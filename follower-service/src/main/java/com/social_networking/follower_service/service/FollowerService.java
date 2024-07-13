package com.social_networking.follower_service.service;

import com.social_networking.follower_service.dto.request.FollowerRequest;
import com.socialnetworking.shared_service.dto.response.BaseResponse;

public interface FollowerService {
    BaseResponse saveFollow(FollowerRequest request);

    BaseResponse unFollow(FollowerRequest request);
}
