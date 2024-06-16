package com.socialnetworking.postservice.repository;

import com.socialnetworking.postservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findCommentByIdAndIsDeletedFalse(Long id);
    List<Comment> findByParentIdAndIsDeletedFalse(Long parentId);
}
