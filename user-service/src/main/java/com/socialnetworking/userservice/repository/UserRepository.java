package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsernameAndIsDeletedFalse(String username);

    List<User> findAllByIsDeletedFalse();

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT f.followingId FROM Follower f WHERE f.followerId = :userId AND f.isFollowing = true) AND u.id != :userId")
    List<User> findUsersNotFollowedByUserId(@Param("userId") Long userId);

//    @Query("SELECT u FROM User u WHERE u.id IN (SELECT f.followingId FROM Follower f WHERE f.followerId = :userId AND f.isFollowing = true) AND u.username LIKE %:keyword%")
//    List<User> findUsersFollowedByUserIdAndUsernameContaining(@Param("userId") Long userId, @Param("keyword") String keyword);
//    @Query("SELECT u FROM User u WHERE u.id IN (SELECT f.followerId FROM Follower f WHERE f.followingId = :userId AND f.isFollowing = true) AND u.username LIKE %:keyword%")
//    List<User> findUsersFollowersOfUserIdAndUsernameContaining(@Param("userId") Long userId, @Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.id IN (SELECT f.followingId FROM Follower f WHERE f.followerId = :userId AND f.isFollowing = true) AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<User> findUsersFollowedByUserIdAndUsernameOrNameContaining(@Param("userId") Long userId, @Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findUsersByKeyword(@Param("keyword") String keyword);
    @Query("SELECT u FROM User u WHERE u.id IN (SELECT f.followerId FROM Follower f WHERE f.followingId = :userId AND f.isFollowing = true) AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<User> findUsersFollowersOfUserIdAndUsernameOrNameContaining(@Param("userId") Long userId, @Param("keyword") String keyword);
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id IN (SELECT f.followerId FROM Follower f WHERE f.followingId = :followingId AND f.followerId = :userId AND f.isFollowing = true)")
    boolean checkUserIsFollowingByUserId(@Param("userId") Long userId, @Param("followingId") Long followingId);

    @Query("SELECT COUNT(f) > 0 FROM Follower f WHERE f.followerId = :userId AND f.followingId = :followingId AND f.isFollowing = true")
    boolean isFollowing(@Param("userId") Long userId, @Param("followingId") Long followingId);


    Optional<User> findUserByUserId(String userId);

    User findUserByIdAndIsDeletedFalse(Long id);

//    User findByIdAnd

    User findByEmail(String email);

    Optional<User> findFirstByOrderByUserIdDesc();


//    @Transactional
//    @Modifying
//    @Query("UPDATE User u SET u.status = :status WHERE u.email = :email")
//    void updateUserStatusByEmail(String email, UserStatus status);
}
