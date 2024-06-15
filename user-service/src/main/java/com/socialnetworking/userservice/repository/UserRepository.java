package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.User;
import com.socialnetworking.userservice.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);


    Optional<User> findUserByUserId(String userId);

    User findByEmail(String email);

    Optional<User> findFirstByOrderByUserIdDesc();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.email = :email")
    void updateUserStatusByEmail(String email, UserStatus status);
}
