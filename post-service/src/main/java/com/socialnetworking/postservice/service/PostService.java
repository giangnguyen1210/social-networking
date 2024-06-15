package com.socialnetworking.postservice.service;

import com.socialnetworking.postservice.dto.request.PostDeleteRequest;
import com.socialnetworking.postservice.dto.request.PostRequest;
import com.socialnetworking.shared_service.dto.response.BaseResponse;

import java.io.IOException;

public interface PostService {
     BaseResponse createPost(PostRequest request) throws IOException;

    BaseResponse createPostSaveDraft(PostRequest postRequest);

    BaseResponse updatePost(PostRequest request);

    BaseResponse getPostByPostId(Long id);

    BaseResponse getPostSaveDraftByUserId(Long id);

    BaseResponse deleteSaveDraft(PostDeleteRequest userId);
    BaseResponse deletePost(PostDeleteRequest postId);
}
