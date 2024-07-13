package com.socialnetworking.postservice.model;

import com.socialnetworking.postservice.model.base.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "photo")
@Entity
public class Photo extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private String mimeType;
    private Long postId;
    private Long userId;
    private Boolean isDeleted;
}
