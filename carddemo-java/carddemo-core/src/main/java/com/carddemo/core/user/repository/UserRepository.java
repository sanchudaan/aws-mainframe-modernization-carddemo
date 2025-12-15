package com.carddemo.core.user.repository;

import com.carddemo.common.constant.UserType;
import com.carddemo.core.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity - replaces VSAM USRSEC file operations.
 * Provides CRUD operations and custom queries for user management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByUserType(UserType userType);

    Page<User> findByUserType(UserType userType, Pageable pageable);

    List<User> findByLastName(String lastName);

    @Query("SELECT u FROM User u WHERE u.lastName LIKE :prefix%")
    List<User> findByLastNameStartingWith(@Param("prefix") String prefix);

    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = :userType")
    long countByUserType(@Param("userType") UserType userType);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> searchByName(@Param("name") String name, Pageable pageable);

    Optional<User> findByUserIdAndPassword(String userId, String password);
}
