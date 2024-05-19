package com.socialnetworking.userservice.service.impl;

import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.response.BaseResponse;
import com.socialnetworking.userservice.model.Gender;
import com.socialnetworking.userservice.model.User;
import com.socialnetworking.userservice.repository.GenderRepository;
import com.socialnetworking.userservice.repository.UserRepository;
import com.socialnetworking.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;
    @Override
    public BaseResponse update(UserEditRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        Optional<User> optionalUser = userRepository.findUserByUserId(request.getUserId());
        User accountUsername = userRepository.findByUsername(request.getUsername());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(accountUsername!=null){
                if(Objects.equals(accountUsername.getUsername(), request.getUsername()) && !Objects.equals(accountUsername.getUserId(), request.getUserId())){
                    baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
                    baseResponse.setErrorDesc("Tên người dùng đã tồn tại");
                    return baseResponse;
                }
            }
            if(request.getUsername()!=null){
                user.setUsername(request.getUsername());
            }
            if(request.getBio()!=null){
                user.setBio(request.getBio());
            }
            if(request.getName()!=null){
                user.setName(request.getName());
            }
            if(request.getBirthday()!=null){
                user.setBirthday(request.getBirthday());
            }
            Integer genderId = request.getGenderId();
            if (genderId != null) {
                Gender gender = genderRepository.findById(genderId).orElse(null);
                user.setGender(gender);
            }

            MultipartFile avatarFile = request.getAvatar();
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String fileName = avatarFile.getOriginalFilename();
                String avatarUrl = "/avatars/" + fileName; // Đường dẫn tạm thời
                user.setAvatarUrl(avatarUrl);
            }

            // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
            userRepository.save(user);

            baseResponse.setData(user); // Chắc chắn rằng bạn đang trả về user1, không phải user
            baseResponse.setErrorCode(HttpStatus.OK.name());
        } else {
            // Xử lý trường hợp không tìm thấy người dùng
        }

        return baseResponse;
    }


    @Override
    public BaseResponse getInfo(String username) {
        BaseResponse baseResponse = new BaseResponse();
        User user = userRepository.findByUsername(username);
        if(user!=null){
            baseResponse.setData(user);
            baseResponse.setErrorCode(HttpStatus.OK.name());
        }
        return baseResponse;
    }
}
