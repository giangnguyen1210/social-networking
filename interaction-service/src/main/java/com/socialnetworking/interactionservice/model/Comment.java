package com.socialnetworking.interactionservice.model;

import com.socialnetworking.interactionservice.model.base.AuditModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Comment extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId; // Field mới để lưu trữ ID của comment gốc (nếu có)
    private String content;
    private Boolean isDeleted = false;
}
