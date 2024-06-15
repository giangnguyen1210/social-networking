package com.socialnetworking.postservice.repository;

import com.socialnetworking.postservice.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
//    List<Photo> findPhotosByPostIdAndIsDeletedFalse(Long postId);
    Photo findPhotoByIdAndPostIdAndIsDeletedFalse(Long id, Long PostId);
}
