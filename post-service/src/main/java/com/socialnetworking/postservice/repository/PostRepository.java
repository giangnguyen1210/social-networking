package com.socialnetworking.postservice.repository;

import com.socialnetworking.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post getPostByIdAndIsDeletedFalse(Long id);

//    List<Post> getPostsByUserIdAndIsDeletedFalseAndIsDraftFalseAndOrderByCreatedAtDesc(Long userId);
    List<Post> findByUserIdAndIsDeletedFalseAndIsDraftFalseOrderByCreatedAtDesc(Long userId);
    Post findFirstByUserIdAndIsDraftTrueAndIsDeletedFalse(Long userId);
    List<Post> findByUserIdInAndIsDeletedFalseAndIsDraftFalseOrderByCreatedAtDesc(List<Long> userIds);
    List<Post> findByUserIdIn(List<Long> userIds);
//    List<Post>

//    Post getPostByUserIdSaveDraft(Long userId);
}
