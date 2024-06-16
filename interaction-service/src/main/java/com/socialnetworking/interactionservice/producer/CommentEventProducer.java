package com.socialnetworking.interactionservice.producer;

import com.socialnetworking.shared_service.dto.response.CommentResponse;
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

    @Value("${rabbitmq.comment_update_exchange.name}")
    private String commentUpdateExchange;
    @Value("${rabbitmq.comment_update_routing.key}")
    private String commentUpdateRoutingKey;

    @Value("${rabbitmq.comment_delete_exchange.name}")
    private String commentDeleteExchange;
    @Value("${rabbitmq.comment_delete_routing.key}")
    private String commentDeleteRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentEventProducer.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendComment(CommentResponse comment) {
        LOGGER.info(String.format("Sending message -> %s", comment));
        rabbitTemplate.convertAndSend(exchange, routingKey, comment);
    }

    public void sendUpdateComment(CommentResponse comment) {
        LOGGER.info(String.format("Sending message -> %s", comment));
        rabbitTemplate.convertAndSend(commentUpdateExchange, commentUpdateRoutingKey, comment);
    }

    public void sendDeleteComment(CommentResponse comment) {
        LOGGER.info(String.format("Sending delete message -> %s", comment));
        rabbitTemplate.convertAndSend(commentDeleteExchange, commentDeleteRoutingKey, comment);
    }
}
