package com.socialnetworking.userservice.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Data
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String otp;
    private LocalDateTime expiryTime;
    private String email;
    private long counter;

    // Getters and setters
}

