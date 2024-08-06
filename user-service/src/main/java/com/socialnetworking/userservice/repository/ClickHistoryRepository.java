package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.ClickHistory;
import com.socialnetworking.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClickHistoryRepository extends JpaRepository<ClickHistory, Long> {
    List<ClickHistory> findByUserId(Long userId);
    boolean existsByUserIdAndClickedUserId(Long userId, Long clickedUserId);
    @Query("SELECT u FROM User u WHERE u.id IN (SELECT ch.clickedUserId FROM ClickHistory ch WHERE ch.userId = :userId)")
    List<User> findUsersByUserIdInClickHistory(Long userId);
}

