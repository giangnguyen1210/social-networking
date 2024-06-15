//package com.socialnetworking.photoservice.consumer;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.socialnetworking.photoservice.dto.request.PhotoRequest;
//import com.socialnetworking.photoservice.dto.response.BaseResponse;
//import com.socialnetworking.photoservice.model.Post;
//import com.socialnetworking.photoservice.service.PhotoService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//
//@Service
//public class PhotoUploadConsumer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoUploadConsumer.class);
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    private PhotoService photoService;
//
////    @RabbitListener(queues = "${rabbitmq.queue.name}")
////    public void handlePostMessage(String message) throws IOException {
////        Post post = objectMapper.readValue(message, Post.class);
////
////        // Extract postId from the received message
////        Long postId = post.getId();
////
////        // Logic to upload photo
////        LOGGER.info(String.format("Received message -> %s", post));
////
////        // Create PhotoRequest with postId
////        PhotoRequest photoRequest = new PhotoRequest();
////        photoRequest.setTitle(post.getTitle());
//////        photoRequest.setFile(post.get());
////        photoRequest.setPostId(postId);
////        photoRequest.setUserId(post.getUserId());
////
////        // Call uploadPhoto method with PhotoRequest
////        photoService.uploadPhoto(photoRequest);
////    }
//}
