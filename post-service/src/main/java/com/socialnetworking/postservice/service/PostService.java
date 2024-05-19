package com.socialnetworking.postservice.service;

import com.socialnetworking.postservice.dto.response.BaseResponse;
import com.socialnetworking.postservice.model.Post;

public interface PostService {
     BaseResponse createPost(Post request);
}
