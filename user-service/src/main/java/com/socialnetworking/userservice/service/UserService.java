package com.socialnetworking.userservice.service;


import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.userservice.dto.request.UserEditAvatar;
import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.request.UserRequest;
import com.socialnetworking.userservice.model.User;

public interface UserService {
    BaseResponse updateInfo(UserEditRequest request);
    BaseResponse updateAvatar(UserEditAvatar request);

    BaseResponse getInfo(UserRequest user);
    BaseResponse getAvatar(UserRequest user);


}
