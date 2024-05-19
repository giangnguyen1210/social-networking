package com.socialnetworking.userservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "`describe`") // Using backticks to escape reserved keyword
    private String describe;
}
