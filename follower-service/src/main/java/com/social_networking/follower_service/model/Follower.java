package com.social_networking.follower_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "followers")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long followerId;
    private Long followingId;
}
