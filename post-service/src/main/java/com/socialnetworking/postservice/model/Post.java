package com.socialnetworking.postservice.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "image_id", nullable = false)
    private Long imageId;
    @ElementCollection
    private List<String> photoUrls;
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;


    // Constructors, Getters, Setters
}