package com.socialnetworking.shared_service.dto.response;

import com.socialnetworking.shared_service.dto.response.base.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse extends AuditModel {
    private Long id;
    private String title;
    private Long postId;
    private Long userId;
    private List<FileData> files;
}
