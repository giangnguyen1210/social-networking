package com.socialnetworking.postservice.model;

import com.socialnetworking.postservice.model.base.AuditModel;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
public class Post extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long userId;
    private Boolean hasPhoto;
    private Boolean isPublish;
    private Boolean isDraft;
    private Boolean isDeleted;
    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();

}