package com.socialnetworking.shared_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileData {
    private byte[] content;
    private String mimeType;
    private String extension;
}
