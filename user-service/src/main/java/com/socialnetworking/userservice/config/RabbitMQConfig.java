package com.socialnetworking.userservice.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.post_following_queue.name}")
    private String postFollowingQueue;
    @Value("${rabbitmq.post_following_exchange.name}")
    private String postFollowingExchange;
    @Value("${rabbitmq.post_following_routing.key}")
    private String postFollowingRoutingKey;
    @Bean
    public Queue queue() {
        return new Queue(queue, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public Queue postFollowingQueue() {
        return new Queue(postFollowingQueue, true);
    }

    @Bean
    public TopicExchange postFollowingExchange() {
        return new TopicExchange(postFollowingExchange);
    }
    @Bean
    public Binding postFollowingBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(postFollowingQueue()).to(postFollowingExchange()).with(postFollowingRoutingKey);
    }
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }
}