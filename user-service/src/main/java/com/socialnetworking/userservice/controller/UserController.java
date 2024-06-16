package com.socialnetworking.userservice.controller;

import com.socialnetworking.shared_service.util.FileUtil;
import com.socialnetworking.userservice.dto.request.UserEditAvatar;
import com.socialnetworking.userservice.dto.request.UserEditRequest;
import com.socialnetworking.userservice.dto.request.UserRequest;
import com.socialnetworking.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/get-info")
    public ResponseEntity<?> getInfo(@RequestBody UserRequest request){
        return new ResponseEntity<>(userService.getInfo(request), HttpStatus.OK);
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

}
