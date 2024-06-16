package com.socialnetworking.userservice.service.impl;

import com.socialnetworking.shared_service.dto.response.AvatarResponse;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.shared_service.dto.response.FileData;
import com.socialnetworking.userservice.dto.request.UserEditAvatar;
import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.request.UserRequest;
import com.socialnetworking.userservice.model.Avatar;
import com.socialnetworking.userservice.model.Gender;
import com.socialnetworking.userservice.model.User;
import com.socialnetworking.userservice.reducer.UserEventProducer;
import com.socialnetworking.userservice.repository.AvatarRepository;
import com.socialnetworking.userservice.repository.GenderRepository;
import com.socialnetworking.userservice.repository.UserRepository;
import com.socialnetworking.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private UserEventProducer userEventProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public BaseResponse updateInfo(UserEditRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        Optional<User> optionalUser = userRepository.findUserByUserId(request.getUserId());
        User accountUsername = userRepository.findUserByUsername(request.getUsername());
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

//            MultipartFile avatarFile = request.getAvatar();
//            if (avatarFile != null && !avatarFile.isEmpty()) {
//                String fileName = avatarFile.getOriginalFilename();
//                String avatarUrl = "/avatars/" + fileName; // Đường dẫn tạm thời
//                user.setAvatarUrl(avatarUrl);
//            }

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
    public BaseResponse updateAvatar(UserEditAvatar request) {
        BaseResponse baseResponse = new BaseResponse();
        Optional<User> userUpdateAvatar = userRepository.findById(request.getUserId());
        if(userUpdateAvatar.isPresent()){
            Avatar checkAvatar= avatarRepository.findAvatarByUserId(request.getUserId());
            if(checkAvatar!=null){
                AvatarResponse avatarResponse = new AvatarResponse();
                avatarResponse.setUserId(request.getUserId());
                avatarResponse.setFile(convertMultipartFile(request.getFile()));
                avatarResponse.setUpdatedAt(LocalDateTime.now());
                avatarResponse.setUpdatedBy(request.getUserId());
                String reply = userEventProducer.sendPost(avatarResponse);
                LOGGER.info(String.format("Reply message -> %s", reply));

                String cleanedReply = reply.replace("[", "").replace("]", "");
                String[] parts = cleanedReply.split("\\+");
                if (parts.length == 2) {
                    String imageUrl = parts[0];
                    String mimeType = parts[1];
                    checkAvatar.setImageUrl(imageUrl);
                    checkAvatar.setUserId(request.getUserId());
                    checkAvatar.setMimeType(mimeType);
                    checkAvatar.setUpdatedBy(request.getUserId());
                    checkAvatar.setUpdatedAt(LocalDateTime.now());
                    avatarRepository.save(checkAvatar);
                    baseResponse.setData(checkAvatar);
                    baseResponse.setErrorCode(HttpStatus.OK.name());
                } else {
                    // Handle error: invalid format
                    throw new IllegalArgumentException("Invalid file information format: ");
                }
            }
            else{
                AvatarResponse avatarResponse = new AvatarResponse();
                avatarResponse.setUserId(request.getUserId());
                avatarResponse.setFile(convertMultipartFile(request.getFile()));
                avatarResponse.setCreatedAt(LocalDateTime.now());
                avatarResponse.setCreatedBy(request.getUserId());
                String reply = userEventProducer.sendPost(avatarResponse);
                String cleanedReply = reply.replace("[", "").replace("]", "");
                String[] parts = cleanedReply.split("\\+");
                if (parts.length == 2) {
                    String imageUrl = parts[0];
                    String mimeType = parts[1];
                    checkAvatar = new Avatar();
                    checkAvatar.setImageUrl(imageUrl);
                    checkAvatar.setMimeType(mimeType);
                    checkAvatar.setUserId(request.getUserId());
                    checkAvatar.setCreatedBy(request.getUserId());
                    checkAvatar.setCreatedAt(LocalDateTime.now());
                    avatarRepository.save(checkAvatar);
                    baseResponse.setData(checkAvatar);
                    baseResponse.setErrorCode(HttpStatus.OK.name());
                } else {
                    // Handle error: invalid format
                    throw new IllegalArgumentException("Invalid file information format: ");
                }

                LOGGER.info(String.format("Reply message -> %s", reply));
            }

        }
        return baseResponse;
    }
    public FileData convertMultipartFile(MultipartFile file) {
        try {
            byte[] content = file.getBytes();
            String mimeType = file.getContentType();
            String extension = getFileExtension(file.getOriginalFilename());
            return new FileData(content, mimeType, extension);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile", e);
        }
    }


    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty() || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public BaseResponse getInfo(UserRequest user) {
        BaseResponse baseResponse = new BaseResponse();
        if(user.getUserId()==null && user.getUsername()==null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            return baseResponse;
        }else{
            User user1 = userRepository.findUserByUsername(user.getUsername());
            baseResponse.setData(user1);
            baseResponse.setErrorCode(HttpStatus.OK.name());
        }
        return baseResponse;
    }

    @Override
    public BaseResponse getAvatar(UserRequest userRequest) {
        BaseResponse baseResponse = new BaseResponse();
        Long userId = userRequest.getId();
        if(userId==null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            return baseResponse;
        }else{
            Avatar avatar = avatarRepository.findAvatarByUserId(userId);
            if(avatar==null){
                baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
                baseResponse.setErrorDesc("No avatar found");
            }else{
                baseResponse.setData(avatar);
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Get avatar success");
            }
        }
        return baseResponse;
    }
}
