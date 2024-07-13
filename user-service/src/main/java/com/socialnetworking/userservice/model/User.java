package com.socialnetworking.userservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String name;
    private Date birthday;
    @Column(unique = true)
    private String username;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private Avatar avatarUrl;
    private Boolean isDeleted = false;
    private Boolean isActive = true;
    // Người dùng đang theo dõi


    @Column(columnDefinition = "TEXT")
    private String bio;
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;


    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "updated_at")
    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;



    // Constructors, Getters, Setters
}
