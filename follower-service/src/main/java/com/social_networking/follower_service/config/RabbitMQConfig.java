package com.social_networking.follower_service.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

    @Value("${rabbitmq.follower_update_queue.name}")
    private String followerUpdateQueue;
    @Value("${rabbitmq.follower_update_exchange.name}")
    private String followerUpdateExchange;
    @Value("${rabbitmq.follower_update_routing.key}")
    private String followerUpdateRoutingKey;


    @Bean
    public Queue queue() {
        return new Queue(queue, true);
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
    public Queue followerUpdateQueue() {
        return new Queue(followerUpdateQueue, true);
    }
    @Bean
    public TopicExchange followerUpdateExchange() {
        return new TopicExchange(followerUpdateExchange);
    }
    @Bean
    public Binding followerUpdateBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(followerUpdateQueue()).to(followerUpdateExchange()).with(followerUpdateRoutingKey);
    }


    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }


}