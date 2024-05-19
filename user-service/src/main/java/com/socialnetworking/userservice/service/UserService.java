package com.socialnetworking.userservice.service;


import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.response.BaseResponse;

public interface UserService {
    BaseResponse update(UserEditRequest request);

    BaseResponse getInfo(String username);


}
