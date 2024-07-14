package com.social_networking.chat_message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChatMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatMessageApplication.class, args);
	}

}
