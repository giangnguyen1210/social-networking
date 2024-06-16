package com.socialnetworking.postservice.repository;

import com.socialnetworking.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post getPostByIdAndIsDeletedFalse(Long id);

    List<Post> getPostsByUserIdAndIsDeletedFalseAndIsDraftFalse(Long userId);
    Post findFirstByUserIdAndIsDraftTrueAndIsDeletedFalse(Long userId);


//    Post getPostByUserIdSaveDraft(Long userId);
}
