package com.socialnetworking.postservice.consumer;

import com.socialnetworking.postservice.model.Like;
import com.socialnetworking.postservice.repository.LikeRepository;
import com.socialnetworking.shared_service.dto.response.LikeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class LikeConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentConsumer.class);
    @Autowired
    private LikeRepository likeRepository;
    @RabbitListener(queues = "${rabbitmq.like_queue.name}")
    public void handleCommentMessage(LikeResponse likeResponse) {
        LOGGER.info("Received message: {}", likeResponse);
        Long postId = likeResponse.getPostId();
        Long userId= likeResponse.getUserId();

        LOGGER.info("Post ID: {}", postId);
        Like like = new Like();
        like.setPostId(postId);
        like.setUserId(userId);
        likeRepository.save(like);
    }
}
