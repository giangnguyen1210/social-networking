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
    private String username;
    private String email;
    private String password;
    private String avatarUrl;
    // Người dùng đang theo dõi
    @ManyToMany
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Set<User> following = new HashSet<>();

    // Người theo dõi người dùng này
    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

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

    @ManyToOne
    @JoinColumn(name= "user_status_id")
    private UserStatus status;


    // Constructors, Getters, Setters
}
