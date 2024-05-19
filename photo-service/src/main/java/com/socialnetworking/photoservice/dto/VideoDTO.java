package com.socialnetworking.photoservice.dto;

import lombok.Data;

import java.io.InputStream;
@Data
public class VideoDTO {
    private String title;
    private InputStream stream;
}
