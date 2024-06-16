package com.socialnetworking.interactionservice.dto.request;

import com.socialnetworking.interactionservice.model.base.AuditModel;
import lombok.Data;

@Data
public class CommentRequest extends AuditModel {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId; // Field mới để lưu trữ ID của comment gốc (nếu có)
    private String content;
}
