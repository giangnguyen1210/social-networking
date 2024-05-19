package com.socialnetworking.postservice.service.impl;

import com.socialnetworking.postservice.dto.response.BaseResponse;
import com.socialnetworking.postservice.model.Post;
import com.socialnetworking.postservice.repository.PostRepository;
import com.socialnetworking.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public BaseResponse createPost(Post request) {
        BaseResponse baseResponse = new BaseResponse();
        Post post = new Post();
        post.setCreatedAt(new Date());

        post = postRepository.save(post);
        if (post.getId() != null) {
            baseResponse.setData(post);
            baseResponse.setErrorCode(HttpStatus.OK.name());
        } else {
            baseResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
            baseResponse.setErrorDesc("Failed to create post");
        }
        return baseResponse;
    }


}
