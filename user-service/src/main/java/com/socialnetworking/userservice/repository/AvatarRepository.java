package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Avatar findAvatarByUserId(Long userId);
}
