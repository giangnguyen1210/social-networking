package com.social_networking.follower_service.reducer;

import com.socialnetworking.shared_service.dto.response.FollowerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FollowerReducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.follower_update_exchange.name}")
    private String updateExchange;

    @Value("${rabbitmq.follower_update_routing.key}")
    private String updateRoutingKey;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowerReducer.class);

    public void sendFollower(FollowerResponse followerResponse) {
        LOGGER.info(String.format("Sending message -> %s", followerResponse));
        rabbitTemplate.convertAndSend(exchange, routingKey, followerResponse);
    }

    public void sendUnFollow(FollowerResponse followerResponse) {
        LOGGER.info(String.format("Sending message -> %s", followerResponse));
        rabbitTemplate.convertAndSend(updateExchange, updateRoutingKey, followerResponse);
    }
}
