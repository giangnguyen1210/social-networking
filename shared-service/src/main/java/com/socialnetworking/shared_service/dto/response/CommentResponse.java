package com.socialnetworking.shared_service.dto.response;

import com.socialnetworking.shared_service.dto.response.base.AuditModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse extends AuditModel {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId; // Field mới để lưu trữ ID của comment gốc (nếu có)
    private String content;
}