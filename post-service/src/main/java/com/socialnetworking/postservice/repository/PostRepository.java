package com.socialnetworking.postservice.repository;

import com.socialnetworking.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post getPostByIdAndIsDeletedFalse(Long id);

//    List<Post> getPostsByUserIdAndIsDeletedFalseAndIsDraftFalseAndOrderByCreatedAtDesc(Long userId);
    List<Post> findByUserIdAndIsDeletedFalseAndIsDraftFalseOrderByCreatedAtDesc(Long userId);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND p.isDeleted = false AND p.isDraft = false AND p.createdAt >= :dateThreshold ORDER BY p.createdAt DESC")
    List<Post> findByUserIdAndIsDeletedFalseAndIsDraftFalseAndCreatedAtWithinTwoDays(@Param("userId") Long userId, @Param("dateThreshold") LocalDateTime dateThreshold);
    Post findFirstByUserIdAndIsDraftTrueAndIsDeletedFalse(Long userId);
    List<Post> findByUserIdInAndIsDeletedFalseAndIsDraftFalseOrderByCreatedAtDesc(List<Long> userIds);
    List<Post> findByUserIdIn(List<Long> userIds);
//    List<Post>

//    Post getPostByUserIdSaveDraft(Long userId);
}
