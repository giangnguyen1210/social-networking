package com.socialnetworking.interactionservice.repository;

import com.socialnetworking.interactionservice.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
    List<Like> findLikesByPostId(Long postId);
    List<Like> findLikesByUserId(Long postId);
}
