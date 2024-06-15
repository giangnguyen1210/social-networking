package com.socialnetworking.postservice.producer;

import com.socialnetworking.shared_service.dto.response.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class PostEventProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    @Value("${rabbitmq.post_update_exchange.name}")
    private String postUpdateExchange;
    @Value("${rabbitmq.post_update_routing.key}")
    private String postUpdateRoutingKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostEventProducer.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;
    public String sendPost(PostResponse postResponse) {
        LOGGER.info(String.format("Sending message -> %s", postResponse));
        return (String) rabbitTemplate.convertSendAndReceive(exchange, routingKey, postResponse);
    }

    public String sendPostUpdate(PostResponse postResponse) {
        LOGGER.info(String.format("Sending message -> %s", postResponse));
        return (String) rabbitTemplate.convertSendAndReceive(postUpdateExchange, postUpdateRoutingKey, postResponse);
    }


}
