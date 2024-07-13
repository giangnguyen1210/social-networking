package com.socialnetworking.interactionservice.config;


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

    @Value("${rabbitmq.comment_update_queue.name}")
    private String commentUpdateQueue;
    @Value("${rabbitmq.comment_update_exchange.name}")
    private String commentUpdateExchange;
    @Value("${rabbitmq.comment_update_routing.key}")
    private String commentUpdateRoutingKey;

    @Value("${rabbitmq.comment_delete_queue.name}")
    private String commentDeleteQueue;
    @Value("${rabbitmq.comment_delete_exchange.name}")
    private String commentDeleteExchange;
    @Value("${rabbitmq.comment_delete_routing.key}")
    private String commentDeleteRoutingKey;

    @Value("${rabbitmq.like_queue.name}")
    private String likeQueue;
    @Value("${rabbitmq.like_exchange.name}")
    private String likeExchange;
    @Value("${rabbitmq.like_routing.key}")
    private String likeRoutingKey;
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
    public Queue commentUpdateQueue() {
        return new Queue(commentUpdateQueue, true);
    }

    @Bean
    public TopicExchange commentUpdateExchange() {
        return new TopicExchange(commentUpdateExchange);
    }
    @Bean
    public Binding commentUpdateBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(commentUpdateQueue()).to(commentUpdateExchange()).with(commentUpdateRoutingKey);
    }

    @Bean
    public Queue commentDeleteQueue() {
        return new Queue(commentDeleteQueue, true);
    }
    @Bean
    public TopicExchange commentDeleteExchange() {
        return new TopicExchange(commentDeleteExchange);
    }
    @Bean
    public Binding commentDeleteBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(commentDeleteQueue()).to(commentDeleteExchange()).with(commentDeleteRoutingKey);
    }

    @Bean
    public Queue likeQueue() {
        return new Queue(likeQueue, true);
    }

    @Bean
    public TopicExchange likeExchange() {
        return new TopicExchange(likeExchange);
    }
    @Bean
    public Binding likeBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(likeRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

}