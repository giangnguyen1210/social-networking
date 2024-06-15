package com.socialnetworking.photoservice.model;

import com.socialnetworking.photoservice.model.base.AuditModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="avatar")
public class Avatar extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private String type;
    private String imageUrl;
    private Boolean isDeleted;

}