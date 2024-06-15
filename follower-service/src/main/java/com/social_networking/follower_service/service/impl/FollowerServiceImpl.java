package com.social_networking.follower_service.service.impl;

import com.social_networking.follower_service.model.Follower;
import com.social_networking.follower_service.repository.FollowerRepository;
import com.social_networking.follower_service.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerServiceImpl implements FollowerService {
    @Autowired
    private FollowerRepository followerRepository;

    public Follower follow(Long followerId, Long followingId) {
        Follower follower = new Follower();
        follower.setFollowerId(followerId);
        follower.setFollowingId(followingId);
        return followerRepository.save(follower);
    }

    public void unfollow(Long followerId, Long followingId) {
        followerRepository.deleteAll(followerRepository.findByFollowerIdAndFollowingId(followerId, followingId));
    }

    public List<Follower> getFollowers(Long userId) {
        return followerRepository.findByFollowingId(userId);
    }

    public List<Follower> getFollowing(Long userId) {
        return followerRepository.findByFollowerId(userId);
    }
}
