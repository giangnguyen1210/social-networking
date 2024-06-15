package com.socialnetworking.postservice.service.impl;

import com.socialnetworking.postservice.dto.request.PostDeleteRequest;
import com.socialnetworking.postservice.dto.request.PostRequest;
import com.socialnetworking.postservice.model.Photo;
import com.socialnetworking.postservice.model.Post;
import com.socialnetworking.postservice.producer.PostEventProducer;
import com.socialnetworking.postservice.repository.PhotoRepository;
import com.socialnetworking.postservice.repository.PostRepository;
import com.socialnetworking.postservice.service.PostService;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.shared_service.dto.response.FileData;
import com.socialnetworking.shared_service.dto.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class PostServiceImpl implements PostService {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PhotoRepository photoRepository;


    @Autowired
    private PostEventProducer postEventProducer;


    public BaseResponse createPost(PostRequest postRequest){
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setUserId(postRequest.getUserId());
        post.setCreatedAt(LocalDateTime.now());
        post.setCreatedBy(post.getUserId());
        post.setIsPublish(true);
        post.setIsDraft(false);
        post.setIsDeleted(false);
        if (postRequest.getFiles() != null && !postRequest.getFiles().isEmpty()) {
            post.setHasPhoto(true);
            post = postRepository.save(post);
            PostResponse postResponse = new PostResponse();
            postResponse.setPostId(post.getId());
            postResponse.setUserId(post.getUserId());
            postResponse.setCreatedAt(LocalDateTime.now());
            postResponse.setCreatedBy(post.getUserId());
            postResponse.setFiles(convertMultipartFiles(postRequest.getFiles()));
            String reply = postEventProducer.sendPost(postResponse);
            System.out.println(reply);
            String cleanedReply = reply.replace("[", "").replace("]", "");
            List<String> fileInfoList = List.of(cleanedReply.split(", "));
            List<Photo> photos = new ArrayList<>();
            for (String fileInfo : fileInfoList) {
                String[] parts = fileInfo.split("\\+");
                if (parts.length == 2) {
                    String imageUrl = parts[0];
                    String mimeType = parts[1];
                    Photo photo = new Photo();
                    photo.setPostId(post.getId());
                    photo.setImageUrl(imageUrl);
                    photo.setMimeType(mimeType);
                    photo.setCreatedAt(LocalDateTime.now());
                    photo.setCreatedBy(post.getUserId());
                    photos.add(photo);
                    photoRepository.save(photo);
                } else {
                    // Handle error: invalid format
                    throw new IllegalArgumentException("Invalid file information format: " + fileInfo);
                }
            }
            post.setPhotos(photos);
        }else{
            post.setHasPhoto(false);
            post = postRepository.save(post);

        }
        return new BaseResponse(post);
    }



    @Override
    public BaseResponse createPostSaveDraft(PostRequest postRequest){
        BaseResponse baseResponse = new BaseResponse();
        Post existingDraft = postRepository.findFirstByUserIdAndIsDraftTrueAndIsDeletedFalse(postRequest.getUserId());
        if(existingDraft!=null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("You can save draft only one record");
        }else{
            Post post = new Post();
            post.setTitle(postRequest.getTitle());
            post.setUserId(postRequest.getUserId());
            post.setCreatedAt(LocalDateTime.now());
            post.setCreatedBy(post.getUserId());
            post.setIsDeleted(false);
            post.setIsDraft(true);
            post.setIsPublish(false);
            if (postRequest.getFiles() != null && !postRequest.getFiles().isEmpty()) {
                post.setHasPhoto(true);
                post = postRepository.save(post);
                PostResponse postResponse = new PostResponse();
                postResponse.setPostId(post.getId());
                postResponse.setUserId(post.getUserId());
                postResponse.setCreatedAt(LocalDateTime.now());
                postResponse.setCreatedBy(post.getUserId());
                postResponse.setFiles(convertMultipartFiles(postRequest.getFiles()));
                String reply = postEventProducer.sendPost(postResponse);
                System.out.println(reply);
                String cleanedReply = reply.replace("[", "").replace("]", "");
                List<String> fileInfoList = List.of(cleanedReply.split(", "));
                List<Photo> photos = new ArrayList<>();
                for (String fileInfo : fileInfoList) {
                    String[] parts = fileInfo.split("\\+");
                    if (parts.length == 2) {
                        String imageUrl = parts[0];
                        String mimeType = parts[1];
                        Photo photo = new Photo();
                        photo.setPostId(post.getId());
                        photo.setImageUrl(imageUrl);
                        photo.setMimeType(mimeType);
                        photo.setCreatedAt(LocalDateTime.now());
                        photo.setCreatedBy(post.getUserId());
                        photos.add(photo);
                        photoRepository.save(photo);
                    } else {
                        // Handle error: invalid format
                        throw new IllegalArgumentException("Invalid file information format: " + fileInfo);
                    }
                }
                post.setPhotos(photos);
            }else{
                post.setHasPhoto(false);
                post = postRepository.save(post);

            }
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setData(post);
        }
        return baseResponse;
    }

    @Override
    public BaseResponse updatePost(PostRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        Post postUpdate = postRepository.getPostByIdAndIsDeletedFalse(request.getId());
        System.out.println(postUpdate);
        if(postUpdate!=null){
            postUpdate.setTitle(request.getTitle());
            postUpdate.setUpdatedBy(request.getUserId());
            postUpdate.setUpdatedAt(LocalDateTime.now());
            postRepository.save(postUpdate);
            if (request.getFiles() != null && !request.getFiles().isEmpty()) {
                PostResponse postResponse = new PostResponse();
                postResponse.setPostId(postUpdate.getId());
                postResponse.setUserId(postUpdate.getUserId());
                postResponse.setCreatedAt(LocalDateTime.now());
                postResponse.setCreatedBy(postUpdate.getUserId());
                postResponse.setFiles(convertMultipartFiles(request.getFiles()));
                String reply = postEventProducer.sendPostUpdate(postResponse);
                System.out.println(reply);
            }
        }else{

        }
        return null;
    }

    private void deletePhotoByPhotoIdAndPostId(Long photoId, Long postId){
        Photo photo = photoRepository.findPhotoByIdAndPostIdAndIsDeletedFalse(photoId, postId);
        photoRepository.delete(photo);
    }

    public List<FileData> convertMultipartFiles(List<MultipartFile> files) {
        return files.stream()
                .map(file -> {
                    try {
                        byte[] content = file.getBytes();
                        String mimeType = file.getContentType();
                        String extension = getFileExtension(file.getOriginalFilename());
                        return new FileData(content, mimeType, extension);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty() || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    @Override
    public BaseResponse getPostByPostId(Long id) {
        Post post = postRepository.getPostByIdAndIsDeletedFalse(id);
        return new BaseResponse(post);
    }

    @Override
    public BaseResponse getPostSaveDraftByUserId(Long id) {
        BaseResponse baseResponse = new BaseResponse();
        Post post = postRepository.findFirstByUserIdAndIsDraftTrueAndIsDeletedFalse(id);
        if(post==null){
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("There is no record that save draft");
        }else{
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setData(post);
        }
        return baseResponse;

    }

    @Override
    public BaseResponse deleteSaveDraft(PostDeleteRequest postDeleteRequest) {
        BaseResponse baseResponse = new BaseResponse();
        Post draftPost = postRepository.findFirstByUserIdAndIsDraftTrueAndIsDeletedFalse(postDeleteRequest.getUserId());
        if (draftPost != null) {
            draftPost.setIsDeleted(true);
            draftPost.setUpdatedAt(LocalDateTime.now());
            draftPost.setUpdatedBy(postDeleteRequest.getUserId());
            postRepository.save(draftPost);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Delete successful");
        } else {
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("No draft post found for userId: " + postDeleteRequest.getUserId());
        }
        return baseResponse;
    }

    @Override
    public BaseResponse deletePost(PostDeleteRequest postDeleteRequest) {
        BaseResponse baseResponse = new BaseResponse();
        Post post = postRepository.getPostByIdAndIsDeletedFalse(postDeleteRequest.getId());
        if(post!=null){
            post.setIsDeleted(true);
            post.setUpdatedAt(LocalDateTime.now());
            post.setUpdatedBy(postDeleteRequest.getUserId());
            postRepository.save(post);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Post deleted successfully");
        } else {
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("There is no record with id " + postDeleteRequest.getId() + " to delete");
        }
        return baseResponse;
    }


}
