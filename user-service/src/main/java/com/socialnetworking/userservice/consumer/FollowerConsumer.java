package com.socialnetworking.userservice.consumer;

import com.socialnetworking.shared_service.dto.response.FollowerResponse;
import com.socialnetworking.userservice.model.Follower;
import com.socialnetworking.userservice.repository.FollowerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowerConsumer.class);
    @Autowired
    private FollowerRepository followerRepository;
    @RabbitListener(queues = "${rabbitmq.follower_queue.name}")
    public void handleFollowMessage(FollowerResponse commentResponse) {
        LOGGER.info("Received message: {}", commentResponse);
        Long followerId = commentResponse.getFollowerId();
        Long followingId = commentResponse.getFollowingId();
        Boolean isFollowing = commentResponse.getIsFollowing();
        Follower checkFollower = followerRepository.findByFollowerIdAndFollowingIdAndIsFollowingFalse(followerId,followingId);
        if(checkFollower==null){
            Follower follower = new Follower();
            follower.setFollowerId(followerId);
            follower.setFollowingId(followingId);
            follower.setIsFollowing(isFollowing);
            followerRepository.save(follower);
        }else{
            checkFollower.setIsFollowing(isFollowing);
            followerRepository.save(checkFollower);
        }
    }

    @RabbitListener(queues = "${rabbitmq.follower_update_queue.name}")
    public void handleUnFollowMessage(FollowerResponse commentResponse) {
        LOGGER.info("Received message: {}", commentResponse);
        Long followerId = commentResponse.getFollowerId();
        Long followingId = commentResponse.getFollowingId();
        Boolean isFollowing = commentResponse.getIsFollowing();
        Follower checkFollower = followerRepository.findByFollowerIdAndFollowingIdAndIsFollowingTrue(followerId,followingId);
        if(checkFollower!=null){
            checkFollower.setIsFollowing(isFollowing);
            followerRepository.save(checkFollower);
        }
    }
}
