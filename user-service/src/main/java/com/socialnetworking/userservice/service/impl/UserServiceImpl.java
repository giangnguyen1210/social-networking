package com.socialnetworking.userservice.service.impl;

import com.socialnetworking.shared_service.dto.response.AvatarResponse;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.shared_service.dto.response.FileData;
import com.socialnetworking.userservice.dto.request.FollowerRequest;
import com.socialnetworking.userservice.dto.request.UserEditAvatar;
import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.request.UserRequest;
import com.socialnetworking.userservice.dto.response.UserResponse;
import com.socialnetworking.userservice.model.*;
import com.socialnetworking.userservice.reducer.UserEventProducer;
import com.socialnetworking.userservice.repository.*;
import com.socialnetworking.userservice.service.UserService;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private ClickHistoryRepository clickHistoryRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public BaseResponse updateInfo(UserEditRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        Optional<User> optionalUser = userRepository.findUserByUserId(request.getUserId());
        User accountUsername = userRepository.findUserByUsernameAndIsDeletedFalse(request.getUsername());
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
            Long genderId = request.getGenderId();
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
    public BaseResponse listGender(){
        BaseResponse baseResponse = new BaseResponse();
        List<Gender> genders = genderRepository.findAll();
        baseResponse.setData(genders);
        baseResponse.setErrorDesc("Lấy thành công danh sách giới tính");
        baseResponse.setErrorCode(HttpStatus.OK.name());
        baseResponse.setTotalRecords(genders.size());
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
                String reply = userEventProducer.sendAvatar(avatarResponse);
                LOGGER.info(String.format("Reply message -> %s", reply));

                String cleanedReply = reply.replace("[", "").replace("]", "");
                String[] parts = cleanedReply.split("\\+");
                if (parts.length == 2) {
                    String imageUrl = parts[0];
                    String mimeType = parts[1];
                    checkAvatar.setImageUrl(imageUrl);
                    checkAvatar.setUserId(request.getUserId());
                    checkAvatar.setMimeType(mimeType);
                    checkAvatar.setFilename(imageUrl);
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
                String reply = userEventProducer.sendAvatar(avatarResponse);
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

    @Override
    public BaseResponse getUsersNotFollowing(Long id) {
        BaseResponse baseResponse = new BaseResponse();
        if(id==null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorCode("Id không được để trống");
        }else{
            List<User> notFollowings = userRepository.findUsersNotFollowedByUserId(id);
            List<UserResponse> userResponses = new ArrayList<>();

            for (User user : notFollowings) {
                try {
                    UserResponse userResponse = getUserInfo(user.getUsername());
                    boolean isFollowing = checkFollowing(id, user.getId());
                    userResponse.setFollowing(isFollowing);
                    userResponses.add(userResponse);
                } catch (IOException e) {
                    // Handle exception
                    baseResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
                    baseResponse.setErrorDesc("Error fetching user info for user: " + user.getUsername());
                    return baseResponse;
                }
            }

            baseResponse.setData(userResponses);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Lấy danh sách chưa following thành công");
            baseResponse.setTotalRecords(userResponses.size());

        }
        return baseResponse;
    }


    public BaseResponse getAllUsersFollowing(Long id, String keyword) {
        BaseResponse baseResponse = new BaseResponse();
        if (id == null) {
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("Id không được để trống");
        } else {
            List<User> followings = userRepository.findUsersFollowedByUserIdAndUsernameOrNameContaining(id, keyword);
            List<UserResponse> userResponses = new ArrayList<>();

            for (User user : followings) {
                try {
                    UserResponse userResponse = getUserInfo(user.getUsername());
                    boolean isFollowing = checkFollowing(id, user.getId());
                    userResponse.setFollowing(isFollowing);
                    userResponses.add(userResponse);
                } catch (IOException e) {
                    // Handle exception
                    baseResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
                    baseResponse.setErrorDesc("Error fetching user info for user: " + user.getUsername());
                    return baseResponse;
                }
            }

            baseResponse.setData(userResponses);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Lấy danh sách người dùng following thành công");
            baseResponse.setTotalRecords(userResponses.size());
        }
        return baseResponse;
    }

    public BaseResponse searchUsersByKeyword(Long userId, String keyword) throws IOException {
        BaseResponse baseResponse = new BaseResponse();
        // Lưu thông tin lịch sử tìm kiếm
//        SearchHistory searchHistory = new SearchHistory();
//        searchHistory.setUserId(userId);
//        searchHistory.setKeyword(keyword);
//        searchHistory.setSearchTime(LocalDateTime.now());
//        searchHistoryRepository.save(searchHistory);

        List<User> users = userRepository.findUsersByKeyword(keyword);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = getUserInfo(user.getUsername());
            boolean isFollowing = checkFollowing(userId, user.getId());
            userResponse.setFollowing(isFollowing);
            userResponses.add(userResponse);
        }
        LOGGER.info(userResponses.toString());

        baseResponse.setData(userResponses);
        baseResponse.setErrorCode(HttpStatus.OK.name());
        baseResponse.setErrorDesc("Lấy danh sách người dùng thành công");
        baseResponse.setTotalRecords(userResponses.size());
        return baseResponse;
    }

    @Override
    public BaseResponse recordClick(Long userId, Long clickedUserId) {
        // Lưu thông tin lịch sử click
        BaseResponse baseResponse = new BaseResponse();

        // Kiểm tra nếu lịch sử click đã tồn tại
        boolean exists = clickHistoryRepository.existsByUserIdAndClickedUserId(userId, clickedUserId);

        if (!exists) {
            ClickHistory clickHistory = new ClickHistory();
            clickHistory.setUserId(userId);
            clickHistory.setClickedUserId(clickedUserId);
            clickHistory.setClickTime(LocalDateTime.now());
            clickHistoryRepository.save(clickHistory);

            baseResponse.setData(clickHistory);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Lưu lịch sử tìm kiếm thành công");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse historySearch(Long userId) {
        BaseResponse baseResponse = new BaseResponse();
        List<ClickHistory> histories = clickHistoryRepository.findByUserId(userId);
        baseResponse.setData(histories);
        baseResponse.setErrorCode(HttpStatus.OK.name());
        baseResponse.setErrorDesc("Lấy lịch sử search thành công");
        return baseResponse;
    }

    public BaseResponse findUsersFromHistory(Long userId) {
        BaseResponse baseResponse = new BaseResponse();
        List<User> users = clickHistoryRepository.findUsersByUserIdInClickHistory(userId);
        baseResponse.setData(users);
        baseResponse.setErrorCode(HttpStatus.OK.name());
        baseResponse.setErrorDesc("Lấy danh sách người dùng từ lịch sử search thành công");
        return baseResponse;
    }

    @Override
    public BaseResponse getAllUsersFollower(Long id, String keyword) {
        BaseResponse baseResponse = new BaseResponse();
        if(id==null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorCode("Id không được để trống");
        }else{
            List<User> followers = userRepository.findUsersFollowersOfUserIdAndUsernameOrNameContaining(id, keyword);
            List<UserResponse> userResponses = new ArrayList<>();
            for (User user : followers) {
                try {
                    UserResponse userResponse = getUserInfo(user.getUsername());
                    boolean isFollowing = checkFollowing(id, user.getId());
                    userResponse.setFollowing(isFollowing);
                    userResponses.add(userResponse);
                } catch (IOException e) {
                    baseResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
                    baseResponse.setErrorDesc("Error fetching user info for user: " + user.getUsername());
                    return baseResponse;
                }
            }

            baseResponse.setData(userResponses);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Lấy danh sách người dùng follower thành công");
            baseResponse.setTotalRecords(userResponses.size());
        }
        return baseResponse;
    }


    @Override
    public BaseResponse getAllUser() {
        BaseResponse baseResponse = new BaseResponse();
        List<User> users = userRepository.findAllByIsDeletedFalse();
        baseResponse.setData(users);
        return baseResponse;
    }

    @Override
    public BaseResponse checkIsFollowing(FollowerRequest followerRequest) {
        BaseResponse baseResponse = new BaseResponse();
        if(followerRequest.getFollowingId()!=null && followerRequest.getUserId()!=null && !followerRequest.getUserId().equals(followerRequest.getFollowingId())){
            boolean isFollowing = userRepository.checkUserIsFollowingByUserId(followerRequest.getUserId(), followerRequest.getFollowingId());
            if(isFollowing){
                baseResponse.setData(true);
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Đang theo dõi");
            }else{
                baseResponse.setData(false);
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Chưa theo dõi");
            }
        }else {
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("Bạn phải nhập đầy đủ thông tin");
        }
        return baseResponse;
    }


    public boolean checkFollowing(Long userId, Long followingId) {
        if(userRepository.checkUserIsFollowingByUserId(userId, followingId)){
            return true;
        }
        return false;
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
    public BaseResponse getUserInfoByUsername(String username) throws IOException {
        BaseResponse baseResponse = new BaseResponse();
        if(username==null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            return baseResponse;
        }else{
            User user = userRepository.findUserByUsernameAndIsDeletedFalse(username);
            if(user!=null){
                Avatar avatar = avatarRepository.findAvatarByUserId(user.getId());
                AvatarResponse avatarResponse = new AvatarResponse();
                if (avatar != null) {
                    String imageUrl = avatar.getImageUrl();
                    byte[] fileContent = FileUtils.readFileToByteArray(new File(imageUrl));
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    avatarResponse.setDataFile(encodedString);
                    avatarResponse.setCreatedAt(avatar.getCreatedAt());
                    avatarResponse.setUpdatedAt(avatar.getUpdatedAt());
                }
                UserResponse userResponse = getUserResponse(avatarResponse, user);
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Get info success");
                baseResponse.setData(userResponse);
            }else{
                baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
                baseResponse.setErrorDesc("User is not exist");
            }

        }
        return baseResponse;
    }

    @Override
    public BaseResponse getUserInfoById(Long id) throws IOException {
        BaseResponse baseResponse = new BaseResponse();
        if(id==null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            return baseResponse;
        }else{
            User user = userRepository.findUserByIdAndIsDeletedFalse(id);
            if(user!=null){
                Avatar avatar = avatarRepository.findAvatarByUserId(user.getId());
                AvatarResponse avatarResponse = new AvatarResponse();
                if (avatar != null) {
                    String imageUrl = avatar.getImageUrl();
                    byte[] fileContent = FileUtils.readFileToByteArray(new File(imageUrl));
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    avatarResponse.setDataFile(encodedString);
                    avatarResponse.setCreatedAt(avatar.getCreatedAt());
                    avatarResponse.setUpdatedAt(avatar.getUpdatedAt());
                }
                UserResponse userResponse = getUserResponse(avatarResponse, user);
                baseResponse.setErrorCode(HttpStatus.OK.name());
                baseResponse.setErrorDesc("Get info success");
                baseResponse.setData(userResponse);
            }else{
                baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
                baseResponse.setErrorDesc("User is not exist");
            }

        }
        return baseResponse;
    }

    private UserResponse getUserInfo(String username) throws IOException {
        User user = userRepository.findUserByUsernameAndIsDeletedFalse(username);
        Avatar avatar = avatarRepository.findAvatarByUserId(user.getId());
        AvatarResponse avatarResponse = new AvatarResponse();
        if (avatar != null) {
            String imageUrl = avatar.getImageUrl();
            byte[] fileContent = FileUtils.readFileToByteArray(new File(imageUrl));
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            avatarResponse.setDataFile(encodedString);
            avatarResponse.setCreatedAt(avatar.getCreatedAt());
            avatarResponse.setUpdatedAt(avatar.getUpdatedAt());
        }
        return getUserResponse(avatarResponse, user);
    }


    @NotNull
    private static UserResponse getUserResponse(AvatarResponse avatarResponse, User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setAvatarData(avatarResponse);
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setBio(user.getBio());
        userResponse.setName(user.getName());
        userResponse.setUsername(user.getUsername());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setCreatedDate(user.getCreatedDate());
        userResponse.setUpdatedDate(user.getUpdatedDate());
        return userResponse;
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
