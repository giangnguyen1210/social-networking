package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.ClickHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickHistoryRepository extends JpaRepository<ClickHistory, Long> {
    List<ClickHistory> findByUserId(Long userId);
}
