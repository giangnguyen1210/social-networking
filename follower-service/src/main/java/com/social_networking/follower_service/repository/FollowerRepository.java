package com.social_networking.follower_service.repository;

import com.social_networking.follower_service.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByFollowerId(Long followerId);
    List<Follower> findByFollowingId(Long followingId);

    Iterable<Follower> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
