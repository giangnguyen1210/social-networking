//package com.socialnetworking.postservice.producer;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RabbitMQSender {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    // Phương thức để gửi một thông điệp tới RabbitMQ
//    public void sendNewPost(Long postId) {
//        rabbitTemplate.convertAndSend("exchange_name", "routing_key", postId);
//    }
//}
