package com.socialnetworking.interactionservice.producer;

import com.socialnetworking.interactionservice.dto.response.CommentResponse;
import com.socialnetworking.interactionservice.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class CommentEventProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentEventProducer.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendComment(Comment comment) {
        LOGGER.info(String.format("Sending message -> %s", comment));
        rabbitTemplate.convertAndSend(exchange, routingKey, comment);
    }


}
