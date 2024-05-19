package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

    Optional<Gender> findById(Integer id);

}
