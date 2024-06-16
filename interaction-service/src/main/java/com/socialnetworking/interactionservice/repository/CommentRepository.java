package com.socialnetworking.interactionservice.repository;


import com.socialnetworking.interactionservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndIsDeletedFalse(Long id);
    List<Comment> getCommentsByPostIdAndIsDeletedFalse(Long postId);
    List<Comment> findByParentIdAndIsDeletedFalse(Long parentId); // Phương thức mới để lấy các replies của một comment

    Comment getCommentByIdAndIsDeletedFalse(Long commentId);



//    List<Comment>


}
