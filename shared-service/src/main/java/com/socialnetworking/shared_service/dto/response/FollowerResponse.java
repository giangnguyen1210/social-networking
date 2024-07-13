package com.socialnetworking.shared_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponse {
    private Long id;
    private Long followerId;
    private Long followingId;
    private Boolean isFollowing = true;
}
