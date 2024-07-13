package com.socialnetworking.shared_service.dto.response;

import com.socialnetworking.shared_service.dto.response.base.AuditModel;
import lombok.Data;

@Data
public class LikeResponse extends AuditModel {
    private Long id;
    private Long postId;
    private Long userId;
}