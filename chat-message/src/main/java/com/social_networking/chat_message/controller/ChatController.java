//package com.social_networking.chat_message.controller;
//
//import com.social_networking.chat_message.model.ChatMessage;
//import com.social_networking.chat_message.model.ChatNotification;
//import com.social_networking.chat_message.service.ChatMessageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/chat-message")
//@RequiredArgsConstructor
//public class ChatController {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//    private final ChatMessageService chatMessageService;
//
//    @MessageMapping("/chat")
//    public void processWebSocketMessage(@Payload ChatMessage chatMessage) {
//        handleMessage(chatMessage);
//    }
//
//    @PostMapping("/chat")
//    public ResponseEntity<ChatMessage> processHttpPostMessage(@RequestBody ChatMessage chatMessage) {
//        ChatMessage savedMsg = handleMessage(chatMessage);
//        return ResponseEntity.ok(savedMsg);
//    }
//
//    private ChatMessage handleMessage(ChatMessage chatMessage) {
//        ChatMessage savedMsg = chatMessageService.save(chatMessage);
//        messagingTemplate.convertAndSendToUser(
//                chatMessage.getRecipientId(), "/queue/messages",
//                new ChatNotification(
//                        savedMsg.getId(),
//                        savedMsg.getSenderId(),
//                        savedMsg.getRecipientId(),
//                        savedMsg.getContent()
//                )
//        );
//        return savedMsg;
//    }
//
//    @GetMapping("/messages/{senderId}/{recipientId}")
//    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
//                                                              @PathVariable String recipientId) {
//        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
//    }
//}
package com.social_networking.chat_message.controller;

import com.social_networking.chat_message.model.ChatMessage;
import com.social_networking.chat_message.model.ChatNotification;
import com.social_networking.chat_message.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-message")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processWebSocketMessage(@Payload ChatMessage chatMessage) {
        handleMessage(chatMessage);
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatMessage> processHttpPostMessage(@RequestBody ChatMessage chatMessage) {
        ChatMessage savedMsg = handleMessage(chatMessage);
        return ResponseEntity.ok(savedMsg);
    }

    private ChatMessage handleMessage(ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
        return savedMsg;
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                              @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
