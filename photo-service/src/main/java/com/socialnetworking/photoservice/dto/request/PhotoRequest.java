package com.socialnetworking.photoservice.dto.request;

import lombok.*;
//import org.bson.types.Binary;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoRequest {
    private Long userId;
    private Long postId;
//    private Binary image;

}
