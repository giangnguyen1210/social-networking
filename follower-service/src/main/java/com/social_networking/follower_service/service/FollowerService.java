package com.social_networking.follower_service.service;

import com.social_networking.follower_service.model.Follower;

public interface FollowerService {
    Follower follow(Long followerId, Long followingId);
    void unfollow(Long followerId, Long followingId);

}
