package com.socialnetworking.postservice.model;

import com.socialnetworking.postservice.model.base.AuditModel;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="likes")
public class Like extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private Long userId;
}