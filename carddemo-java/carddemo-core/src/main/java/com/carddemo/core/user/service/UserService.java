package com.carddemo.core.user.service;

import com.carddemo.common.constant.UserType;
import com.carddemo.core.user.entity.User;
import com.carddemo.core.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Service for User operations - replaces COBOL programs:
 * - COSGN00C.cbl (Sign On)
 * - COUSR00C.cbl (User Menu)
 * - COUSR01C.cbl (User Add)
 * - COUSR02C.cbl (User Update)
 * - COUSR03C.cbl (User Delete)
 */
@Service
@Transactional
public class UserService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<User> findByUserType(UserType userType) {
        return userRepository.findByUserType(userType);
    }

    @Transactional(readOnly = true)
    public Page<User> findByUserType(UserType userType, Pageable pageable) {
        return userRepository.findByUserType(userType, pageable);
    }

    @Transactional(readOnly = true)
    public Page<User> searchByName(String name, Pageable pageable) {
        return userRepository.searchByName(name, pageable);
    }

    public User save(User user) {
        updateTimestamp(user);
        return userRepository.save(user);
    }

    public User create(User user) {
        if (user.getUserId() != null && userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User already exists with ID: " + user.getUserId());
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        updateTimestamp(user);
        return userRepository.save(user);
    }

    public User update(User user) {
        if (user.getUserId() == null || !userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User not found with ID: " + user.getUserId());
        }
        updateTimestamp(user);
        return userRepository.save(user);
    }

    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    public boolean authenticate(String userId, String password) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User changePassword(String userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid current password");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        updateTimestamp(user);
        return userRepository.save(user);
    }

    public User resetPassword(String userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        updateTimestamp(user);
        return userRepository.save(user);
    }

    public User updateUserType(String userId, UserType userType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        user.setUserType(userType);
        updateTimestamp(user);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public long countByUserType(UserType userType) {
        return userRepository.countByUserType(userType);
    }

    @Transactional(readOnly = true)
    public long countAdmins() {
        return userRepository.countByUserType(UserType.ADMIN);
    }

    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.countByUserType(UserType.USER);
    }

    private void updateTimestamp(User user) {
        user.setLastUpdateDate(LocalDate.now().format(DATE_FORMATTER));
        user.setLastUpdateTime(LocalTime.now().format(TIME_FORMATTER));
    }
}
