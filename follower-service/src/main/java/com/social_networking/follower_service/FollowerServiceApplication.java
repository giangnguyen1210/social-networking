package com.social_networking.follower_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FollowerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FollowerServiceApplication.class, args);
	}

}
