package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.Role;
import com.socialnetworking.userservice.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long id);

}
