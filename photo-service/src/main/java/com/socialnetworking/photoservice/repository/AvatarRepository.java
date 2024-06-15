package com.socialnetworking.photoservice.repository;

import com.socialnetworking.photoservice.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Avatar findAvatarByUserId(Long userId);
    @Modifying
    @Transactional
    @Query("UPDATE Avatar a SET a.imageUrl = :imageUrl WHERE a.userId = :userId")
    void updateAvatar(String imageUrl, Long userId);
}
