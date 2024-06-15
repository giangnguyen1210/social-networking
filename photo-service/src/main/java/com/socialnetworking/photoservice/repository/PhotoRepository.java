package com.socialnetworking.photoservice.repository;

import com.socialnetworking.photoservice.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findPhotoByPostIdAndIsDeletedFalse(Long id);


}
