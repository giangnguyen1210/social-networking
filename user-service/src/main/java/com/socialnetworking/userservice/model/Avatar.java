package com.socialnetworking.userservice.model;

import com.socialnetworking.userservice.model.base.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "avatar")
@Entity
public class Avatar extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private String mimeType;
    private Long userId;
}