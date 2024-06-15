package com.socialnetworking.interactionservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private Long userId;
}
