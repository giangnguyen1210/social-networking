package com.socialnetworking.shared_service.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/png", "image/gif",
            "video/mp4", "video/quicktime", "video/x-msvideo"
    ));

    public static boolean isAllowedMimeType(MultipartFile file) {
        return ALLOWED_MIME_TYPES.contains(file.getContentType());
    }
}
