package com.socialnetworking.interactionservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentResponse {
    private Long postId;
    private Long userId;
    private Long parentId; // Field mới để lưu trữ ID của comment gốc (nếu có)
    private String content;
    private LocalDateTime createdAt;
}
