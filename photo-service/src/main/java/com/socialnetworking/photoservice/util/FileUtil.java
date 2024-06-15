package com.socialnetworking.photoservice.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FileUtil {

    public static MultipartFile convertToMultipartFile(byte[] fileContent, String fileName) throws IOException {
        return new MockMultipartFile(fileName, fileName, "application/octet-stream", new ByteArrayInputStream(fileContent));
    }
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty() || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}