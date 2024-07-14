package com.socialnetworking.userservice.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "gender")
@Data
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gender;
}
