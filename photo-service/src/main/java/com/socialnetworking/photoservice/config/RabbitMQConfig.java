package com.socialnetworking.photoservice.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialnetworking.photoservice.service.impl.PhotoServiceImpl;
import com.socialnetworking.postservice.consumer.CommentConsumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.post_update_queue.name}")
    private String postUpdateQueue;

    @Value("${rabbitmq.avatar_queue.name}")
    private String avatarQueue;
    @Bean
    public Queue queue() {
        return new Queue(queue, false);
    }

    @Bean
    public Queue avatarQueue() {
        return new Queue(avatarQueue, false);
    }

    @Bean
    public Queue postUpdateQueue() {
        return new Queue(postUpdateQueue, false);
    }
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }
//
//    @Bean
//    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter postListenerAdapter, MessageListenerAdapter avatarListenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queue, avatarQueue);
//        container.setMessageListener(postListenerAdapter); // Adjust if you need different listeners per queue
//        container.setMessageListener(avatarListenerAdapter);
//        return container;
//    }
//
//    @Bean
//    public MessageListenerAdapter postListenerAdapter(PhotoServiceImpl receiver, MessageConverter converter) {
//        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "handlePostMessage");
//        adapter.setMessageConverter(converter);
//        return adapter;
//    }
//
//    @Bean
//    public MessageListenerAdapter avatarListenerAdapter(PhotoServiceImpl receiver, MessageConverter converter) {
//        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "handleAvatarMessage");
//        adapter.setMessageConverter(converter);
//        return adapter;
//    }

}