package com.socialnetworking.userservice.controller;

import com.socialnetworking.shared_service.util.FileUtil;
import com.socialnetworking.userservice.dto.request.FollowerRequest;
import com.socialnetworking.userservice.dto.request.UserEditAvatar;
import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.request.UserRequest;
import com.socialnetworking.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-info/{username}")
    public ResponseEntity<?> getInfo(@PathVariable ("username") String username) throws IOException {
        return new ResponseEntity<>(userService.getUserInfoByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/get-info-by-id/{id}")
    public ResponseEntity<?> getInfoById(@PathVariable ("id") Long id) throws IOException {
        return new ResponseEntity<>(userService.getUserInfoById(id), HttpStatus.OK);
    }

    @PostMapping("/get-avatar")
    public ResponseEntity<?> getAvatar(@RequestBody UserRequest request){
        return new ResponseEntity<>(userService.getAvatar(request), HttpStatus.OK);
    }

    @PostMapping("/update-info")
    public ResponseEntity<?> updateInfo(@RequestBody UserEditRequest request){
        return new ResponseEntity<>(userService.updateInfo(request), HttpStatus.OK);
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("files") MultipartFile file,
                                     @RequestParam("userId") Long userId){
        if (!FileUtil.isAllowedMimeType(file)) {
            return new ResponseEntity<>("File type not supported: " + file.getContentType(), HttpStatus.BAD_REQUEST);
        }
        UserEditAvatar userEditAvatar = new UserEditAvatar();
        userEditAvatar.setFile(file);
        userEditAvatar.setUserId(userId);
        return new ResponseEntity<>(userService.updateAvatar(userEditAvatar), HttpStatus.OK);
    }

    @GetMapping("/get-users-not-following/{id}")
    public ResponseEntity<?> getAllUsersNotFollowing(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUsersNotFollowing(id), HttpStatus.OK);
    }

    @GetMapping("/get-users-following/{id}")
    public ResponseEntity<?> getAllUsersFollowing(@PathVariable Long id, @RequestParam("keyword") String keyword){
        return new ResponseEntity<>(userService.getAllUsersFollowing(id, keyword), HttpStatus.OK);
    }

    @GetMapping("/search-users/{userId}")
    public ResponseEntity<?> searchUsers(@PathVariable("userId") Long userId,@RequestParam("keyword") String keyword) throws IOException {
        return new ResponseEntity<>(userService.searchUsersByKeyword(userId,keyword), HttpStatus.OK);
    }
//    @GetMapping("/search-username/{username}")
//    public ResponseEntity<?> searchUserByUsername(@PathVariable String username){
//        return new ResponseEntity<>(userService.searchUserByUsername(username), HttpStatus.OK);
//    }


    @GetMapping("/get-users-follower/{id}")
    public ResponseEntity<?> getAllUsersFollower(@PathVariable Long id, @RequestParam("keyword") String keyword){
        return new ResponseEntity<>(userService.getAllUsersFollower(id, keyword), HttpStatus.OK);
    }

//    @GetMapping("/check-is-following")
//    public ResponseEntity<?> checkIsFollowing(@RequestBody FollowerRequest followerRequest){
//        return new ResponseEntity<>(userService.checkIsFollowing(followerRequest), HttpStatus.OK);
//    }

    @GetMapping("/check-is-following/{userId}/{followerId}")
    public ResponseEntity<?> checkIsFollowing(@PathVariable Long userId, @PathVariable Long followerId) {
        FollowerRequest followerRequest = new FollowerRequest(userId, followerId);
        return new ResponseEntity<>(userService.checkIsFollowing(followerRequest), HttpStatus.OK);
    }

    @GetMapping("/list-gender")
    public ResponseEntity<?> listGender(){
        return new ResponseEntity<>(userService.listGender(), HttpStatus.OK);
    }

}
