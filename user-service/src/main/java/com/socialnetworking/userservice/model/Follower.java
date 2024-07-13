package com.socialnetworking.userservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "followers")
public class Follower{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long followerId;
    private Long followingId;
    private Boolean isFollowing = true;
}
