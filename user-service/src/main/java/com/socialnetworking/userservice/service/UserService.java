package com.socialnetworking.userservice.service;


import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.userservice.dto.request.FollowerRequest;
import com.socialnetworking.userservice.dto.request.UserEditAvatar;
import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.request.UserRequest;

import java.io.IOException;

public interface UserService {
    BaseResponse updateInfo(UserEditRequest request);
    BaseResponse updateAvatar(UserEditAvatar request);
    BaseResponse getUsersNotFollowing(Long id);
    BaseResponse getAllUsersFollowing(Long id, String keyword);
    BaseResponse searchUsersByKeyword(Long id, String keyword) throws IOException;

    BaseResponse recordClick(Long userId, Long clickedUserId);

    BaseResponse historySearch(Long userId);
    BaseResponse findUsersFromHistory(Long userId);

    BaseResponse getAllUsersFollower(Long id, String keyword);

    BaseResponse getAllUser();
    BaseResponse checkIsFollowing(FollowerRequest followerRequest);


    BaseResponse getUserInfoByUsername(String username) throws IOException;
    BaseResponse getUserInfoById(Long id) throws IOException;

    BaseResponse getAvatar(UserRequest user);
    BaseResponse listGender();


    BaseResponse deleteSearchHistory(Long userId);
    BaseResponse deleteSearchHistoryClickId(Long userId, Long clickId);
}
