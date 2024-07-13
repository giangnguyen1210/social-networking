package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower,Long> {
    List<Follower> findFollowersByFollowerId(Long id);
    List<Follower> findFollowerByFollowingId(Long id);
    Follower findByFollowerIdAndFollowingIdAndIsFollowingFalse(Long followerId, Long followingId);
    Follower findByFollowerIdAndFollowingIdAndIsFollowingTrue(Long followerId, Long followingId);

}
