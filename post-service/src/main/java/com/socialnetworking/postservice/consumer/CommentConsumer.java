package com.socialnetworking.postservice.consumer;

import com.socialnetworking.postservice.model.Comment;
import com.socialnetworking.postservice.repository.CommentRepository;
import com.socialnetworking.shared_service.dto.response.CommentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentConsumer.class);
    @Autowired
    private CommentRepository commentRepository;
    @RabbitListener(queues = "${rabbitmq.comment_queue.name}")
    public void handleCommentMessage(CommentResponse commentResponse) {
        LOGGER.info("Received message: {}", commentResponse);
        Long postId = commentResponse.getPostId();
        Long userId= commentResponse.getUserId();
        Long parentId = commentResponse.getParentId();
        String content = commentResponse.getContent();

        LOGGER.info("Post ID: {}", postId);
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setCreatedBy(userId);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @RabbitListener(queues = "${rabbitmq.comment_update_queue.name}")
    public void handleCommentUpdateMessage(CommentResponse commentResponse) {
        LOGGER.info("Received message: {}", commentResponse);
        String content = commentResponse.getContent();
        LOGGER.info("Post ID: {}", content);
        Long id = commentResponse.getId();
        Comment comment = commentRepository.findCommentByIdAndIsDeletedFalse(id);


        if(comment!=null){
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setUpdatedBy(commentResponse.getUpdatedBy());
            comment.setContent(content);
            commentRepository.save(comment);

        }

    }

    @RabbitListener(queues = "${rabbitmq.comment_delete_queue.name}")
    public void handleCommentDeleteMessage(CommentResponse commentResponse) {
        LOGGER.info("Delete Received message: {}", commentResponse);
        Long id = commentResponse.getId();
        Comment comment = commentRepository.findCommentByIdAndIsDeletedFalse(id);
        List<Comment> comments = commentRepository.findByParentIdAndIsDeletedFalse(id);
        LOGGER.info("Comment: {}", comments);
        if(comment!=null){
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setUpdatedBy(comment.getCreatedBy());
            comment.setIsDeleted(true);
            commentRepository.save(comment);
            if(comments!=null){
                for(Comment com: comments){
                    com.setIsDeleted(true);
                    com.setUpdatedAt(LocalDateTime.now());
                    com.setUpdatedBy(com.getCreatedBy());
                    commentRepository.save(com);
                }
            }
        }
    }


}
