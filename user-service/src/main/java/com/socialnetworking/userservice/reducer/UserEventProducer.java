package com.socialnetworking.userservice.reducer;

import com.socialnetworking.shared_service.dto.response.AvatarResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.post_following_exchange.name}")
    private String postFollowingExchange;

    @Value("${rabbitmq.post_following_routing.key}")
    private String postFollowingRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventProducer.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;
    public String sendAvatar(AvatarResponse avatarResponse) {
        LOGGER.info(String.format("Sending message -> %s", avatarResponse));
        return (String) rabbitTemplate.convertSendAndReceive(exchange, routingKey, avatarResponse);
    }

//    public String sendFollowing(UserResponse UserResponse) {
//        LOGGER.info(String.format("Sending message -> %s", UserResponse));
//        return (String) rabbitTemplate.convertSendAndReceive(postFollowingExchange, postFollowingRoutingKey, UserResponse);
//    }



}
