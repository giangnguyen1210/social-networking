package com.social_networking.follower_service.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "followers")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long followerId;
    private Long followingId;
    private Boolean isFollowing = true;
}
