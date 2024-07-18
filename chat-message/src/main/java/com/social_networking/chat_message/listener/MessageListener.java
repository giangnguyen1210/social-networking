package com.social_networking.chat_message.listener;

import com.social_networking.chat_message.model.ChatMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "messageQueue")
    public void receiveMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
