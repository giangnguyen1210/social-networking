package com.socialnetworking.interactionservice.producer;

import com.socialnetworking.shared_service.dto.response.LikeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class LikeEventProducer {

    @Value("${rabbitmq.like_exchange.name}")
    private String exchange;

    @Value("${rabbitmq.like_routing.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(LikeEventProducer.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendLike(LikeResponse likeResponse) {
        LOGGER.info(String.format("Sending message -> %s", likeResponse));
        rabbitTemplate.convertAndSend(exchange, routingKey, likeResponse);
    }


}
