package com.socialnetworking.photoservice.service.impl;

import com.socialnetworking.photoservice.dto.response.BaseResponse;
import com.socialnetworking.photoservice.model.Avatar;
import com.socialnetworking.photoservice.model.Photo;
import com.socialnetworking.photoservice.repository.AvatarRepository;
import com.socialnetworking.photoservice.repository.PhotoRepository;
import com.socialnetworking.photoservice.service.PhotoService;
import com.socialnetworking.photoservice.util.FileUtil;
import com.socialnetworking.shared_service.dto.response.AvatarResponse;
import com.socialnetworking.shared_service.dto.response.FileData;
import com.socialnetworking.shared_service.dto.response.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.upload-dir.avatar}")
    private String avatarDir;
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoServiceImpl.class);
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public String handlePostMessage(PostResponse postResponse) {

        LOGGER.info("Received message: {}", postResponse);
        List<String> rep = new ArrayList<>();
        Long postId = postResponse.getPostId();
        Long userId= postResponse.getUserId();
        LocalDateTime createdAt = postResponse.getCreatedAt();
        LOGGER.info("Post ID: {}", postId);
//        List<byte[]> imageFiles = postResponse.getFiles();
        List<FileData> imageFiles = postResponse.getFiles();
        LOGGER.info("Number of image files: {}", imageFiles.size());

        for (int i = 0; i < imageFiles.size(); i++) {
            try {
                FileData fileData = imageFiles.get(i);
                byte[] fileContent = fileData.getContent();
                String mimeType = fileData.getMimeType();
                String extension = fileData.getExtension();
                LOGGER.info("MIME Type: {}", mimeType);
                MultipartFile multipartFile = FileUtil.convertToMultipartFile(fileContent, "file_" + postId + "_" + i + "." + extension);

                String fileName = saveFile(multipartFile);
                String imageUrl = uploadDir + fileName;

                Photo photo = new Photo();
                photo.setImageUrl(imageUrl);
                photo.setPostId(postId);
                photo.setUserId(userId);
                photo.setName(multipartFile.getOriginalFilename());
                photo.setType(mimeType);
                photo.setCreatedAt(createdAt);
                photo.setIsDeleted(false);
                photo.setCreatedBy(userId);
                photoRepository.save(photo);
                LOGGER.info("Saved photo: {}", photo);
                rep.add(imageUrl+ "+"+mimeType);


            } catch (IOException e) {
                LOGGER.error("Error saving file: {}", e.getMessage(), e);
            }
        }
        return rep.toString();
    }

    @RabbitListener(queues = "${rabbitmq.post_update_queue.name}")
    public String handlePostUpdateMessage(PostResponse postResponse) {
        try {
            // Your business logic here
            LOGGER.info("Update Received message: {}", postResponse);
            List<String> rep = new ArrayList<>();
            Long postId = postResponse.getPostId();
            Long userId= postResponse.getUserId();
            LocalDateTime createdAt = postResponse.getCreatedAt();
            LOGGER.info("Update Post ID: {} {}", postId, userId);
            List<FileData> imageFiles = postResponse.getFiles();
            List<Photo> findPhoto = photoRepository.findByPostIdAndIsDeletedFalse(postId);
            if(findPhoto!=null){
                photoRepository.deleteAll(findPhoto);
            }
            if(imageFiles!=null){
                LOGGER.info("Update Number of image files: {}", imageFiles.size());
                for (int i = 0; i < imageFiles.size(); i++) {

                    try {
                        FileData fileData = imageFiles.get(i);
                        byte[] fileContent = fileData.getContent();
                        String mimeType = fileData.getMimeType();
                        String extension = fileData.getExtension();
                        LOGGER.info("MIME Type: {}", mimeType);
                        MultipartFile multipartFile = FileUtil.convertToMultipartFile(fileContent, "file_" + postId + "_" + i + "." + extension);

                        String fileName = saveFile(multipartFile);
                        String imageUrl = uploadDir + fileName;

                        Photo photo = new Photo();
                        photo.setImageUrl(imageUrl);
                        photo.setPostId(postId);
                        photo.setUserId(userId);
                        photo.setName(multipartFile.getOriginalFilename());
                        photo.setType(mimeType);
                        photo.setUpdatedAt(createdAt);
                        photo.setUpdatedBy(userId);
                        photo.setCreatedAt(createdAt);
                        photo.setCreatedBy(userId);
                        photo.setIsDeleted(false);
                        photoRepository.save(photo);
                        LOGGER.info("Saved photo: {}", photo);
                        rep.add(imageUrl+ "+"+mimeType);


                    } catch (IOException e) {
                        LOGGER.error("Error saving file: {}", e.getMessage(), e);
                    }
                }
            }
            // Perform operations based on the postResponse



            return rep.toString();
        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process message", e);
        }

    }

    @RabbitListener(queues = "${rabbitmq.avatar_queue.name}")
    public String handleAvatarMessage(AvatarResponse avatarResponse) throws IOException {
        System.out.println("avatarResponse"+ avatarResponse);
        LOGGER.info("Received message: {}", avatarResponse);
        List<String> rep = new ArrayList<>();
        Long userId= avatarResponse.getUserId();
        LOGGER.info("Post ID: {}", userId);
        FileData imageFile = avatarResponse.getFile();
        byte[] fileContent = imageFile.getContent();
        String mimeType = imageFile.getMimeType();
        String extension = imageFile.getExtension();
        LOGGER.info("MIME Type: {}", mimeType);
        MultipartFile multipartFile = FileUtil.convertToMultipartFile(fileContent, "file_" + userId + "." + extension);
        String fileName = saveFileAvatar(multipartFile);
        String imageUrl = avatarDir + fileName;

        Avatar checkAvatar = avatarRepository.findAvatarByUserId(userId);
        if (checkAvatar != null) {
            checkAvatar.setImageUrl(imageUrl);
            checkAvatar.setUserId(userId);
            checkAvatar.setName(multipartFile.getOriginalFilename());
            checkAvatar.setType(mimeType);
            checkAvatar.setUpdatedAt(avatarResponse.getUpdatedAt());
            checkAvatar.setUpdatedBy(userId);
        } else {
            checkAvatar = new Avatar();
            checkAvatar.setImageUrl(imageUrl);
            checkAvatar.setUserId(userId);
            checkAvatar.setName(multipartFile.getOriginalFilename());
            checkAvatar.setType(mimeType);
            checkAvatar.setCreatedAt(avatarResponse.getCreatedAt());
            checkAvatar.setCreatedBy(userId);
        }
        avatarRepository.save(checkAvatar);

        rep.add(imageUrl+ "+"+mimeType);

        return rep.toString();
    }



    private String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private String saveFileAvatar(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(avatarDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public Optional<Photo> getPhotoById(Long photoId) {
        return photoRepository.findById(photoId);
    }


    private void deletePhoto(Long photoId) {
        BaseResponse baseResponse = new BaseResponse();
        photoRepository.deleteById(photoId);
    }


}
