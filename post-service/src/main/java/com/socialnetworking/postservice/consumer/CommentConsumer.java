package com.socialnetworking.postservice.consumer;

import com.socialnetworking.postservice.model.Comment;
import com.socialnetworking.postservice.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentConsumer.class);
    @Autowired
    private CommentRepository commentRepository;
    @RabbitListener(queues = "${rabbitmq.comment_queue.name}")
    public void handleCommentMessage(Comment comment) {
        LOGGER.info("Received message: {}", comment);
        Long postId = comment.getPostId();
        Long userId= comment.getUserId();
        LOGGER.info("Post ID: {}", postId);
        commentRepository.save(comment);
    }
}
