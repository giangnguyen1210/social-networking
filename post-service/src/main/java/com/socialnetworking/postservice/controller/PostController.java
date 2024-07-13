package com.socialnetworking.postservice.controller;

import com.socialnetworking.postservice.dto.request.PostDeleteRequest;
import com.socialnetworking.postservice.dto.request.PostRequest;
import com.socialnetworking.postservice.service.PostService;
import com.socialnetworking.postservice.util.FileUtil;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @PostMapping( "/create")
    public ResponseEntity<?> createPost(@RequestParam(value = "files", required = false) List<MultipartFile> files,
                                        @RequestParam(value = "userId") Long userId,
                                        @RequestParam(value = "title", required = false) String title) throws IOException {
        PostRequest postRequest = new PostRequest();
        if(files!=null){
            for (MultipartFile file : files) {
                if (!FileUtil.isAllowedMimeType(file)) {
                    return new ResponseEntity<>("File type not supported: " + file.getContentType(), HttpStatus.BAD_REQUEST);
                }
            }
            postRequest.setFiles(files);
        }
        postRequest.setTitle(title);
        postRequest.setUserId(userId);

        return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.OK);
    }

    @PostMapping( "/create-save-draft")
    public ResponseEntity<?> createPostSaveDraft(@RequestParam(value = "files", required = false) List<MultipartFile> files,
                                                @RequestParam(value = "userId") Long userId,
                                                @RequestParam(value = "title", required = false) String title){

        PostRequest postRequest = new PostRequest();
        if(files!=null){
            for (MultipartFile file : files) {
                if (!FileUtil.isAllowedMimeType(file)) {
                    return new ResponseEntity<>("File type not supported: " + file.getContentType(), HttpStatus.BAD_REQUEST);
                }
            }
            postRequest.setFiles(files);
        }
        postRequest.setTitle(title);
        postRequest.setUserId(userId);

        return new ResponseEntity<>(postService.createPostSaveDraft(postRequest), HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getPostByPostId(@PathVariable("id") Long id) throws IOException {
        return new ResponseEntity<>(postService.getPostByPostId(id), HttpStatus.OK);
    }
    @GetMapping("/get-posts-by-user-id/{id}")
    public ResponseEntity<?> getPostByUserId(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(postService.getPostsByUserId(userId), HttpStatus.OK);
    }
//    @GetMapping("/get-posts-by-follower/{id}")
//    public ResponseEntity<?> getPostByFollower(@PathVariable("id") Long followingId){
//        return new ResponseEntity<>(postService.(followingId),HttpStatus.OK);
//    }

//    @PostMapping("/following")
//    public ResponseEntity<BaseResponse> getPostsByFollowingIds(@RequestBody List<Long> followingIds) {
//        BaseResponse response = postService.getPostsByFollowingIds(followingIds);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
    @GetMapping("/get-save-draft/{id}")
    public ResponseEntity<?> getPostSaveDraft(@PathVariable("id") Long id) {
        return new ResponseEntity<>(postService.getPostSaveDraftByUserId(id), HttpStatus.OK);
    }
    @PostMapping("/delete-save-draft")
    public ResponseEntity<?> deleteDraft(@RequestBody PostDeleteRequest postRequest) {
        BaseResponse response = postService.deleteSaveDraft(postRequest);
        HttpStatus status = response.getErrorCode().equals(HttpStatus.OK.name()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }
    @PostMapping("/delete")
    public ResponseEntity<BaseResponse> deletePost(@RequestBody PostDeleteRequest postDeleteRequest) {
        BaseResponse response = postService.deletePost(postDeleteRequest);
        HttpStatus status = response.getErrorCode().equals(HttpStatus.OK.name()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }
    @PostMapping("/update")
    public ResponseEntity<?> updatePost(@RequestParam(value = "files", required = false) List<MultipartFile> files,
                                        @RequestParam(value = "userId") Long userId,
                                        @RequestParam(value = "id") Long id,
                                        @RequestParam(value = "title", required = false) String title) {

        PostRequest postRequest = new PostRequest();
        if(files!=null){
            for (MultipartFile file : files) {
                if (!FileUtil.isAllowedMimeType(file)) {
                    return new ResponseEntity<>("File type not supported: " + file.getContentType(), HttpStatus.BAD_REQUEST);
                }
            }
            postRequest.setFiles(files);
        }
        postRequest.setId(id);
        postRequest.setTitle(title);
        postRequest.setUserId(userId);
        return new ResponseEntity<>(postService.updatePost(postRequest), HttpStatus.OK);
    }
    @PostMapping( "/update-save-draft")
    public ResponseEntity<?> updatePostSaveDraft(@RequestParam(value = "files", required = false) List<MultipartFile> files,
                                                 @RequestParam(value = "userId") Long userId,
                                                 @RequestParam(value = "id") Long id,
                                                     @RequestParam(value = "title", required = false) String title) throws IOException {

        PostRequest postRequest = new PostRequest();
        if(files!=null){
            for (MultipartFile file : files) {
                if (!FileUtil.isAllowedMimeType(file)) {
                    return new ResponseEntity<>("File type not supported: " + file.getContentType(), HttpStatus.BAD_REQUEST);
                }
            }
            postRequest.setFiles(files);
        }
        postRequest.setId(id);
        postRequest.setTitle(title);
        postRequest.setUserId(userId);

        return new ResponseEntity<>(postService.updatePostSaveDraft(postRequest), HttpStatus.OK);
    }
}
