package com.socialnetworking.shared_service.dto.response;

import com.socialnetworking.shared_service.dto.response.base.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvatarResponse extends AuditModel {
    private Long id;
    private Long userId;
    private String mimeType;
    private FileData file;
    private String dataFile;
}
