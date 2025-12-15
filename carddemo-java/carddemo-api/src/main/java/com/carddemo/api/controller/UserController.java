package com.carddemo.api.controller;

import com.carddemo.api.dto.AuthRequest;
import com.carddemo.api.dto.AuthResponse;
import com.carddemo.api.dto.UserDto;
import com.carddemo.common.constant.UserType;
import com.carddemo.core.user.entity.User;
import com.carddemo.core.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User operations - replaces CICS transactions:
 * - CC00 (Sign On) -> POST /api/users/auth
 * - CM00 (User Menu) -> GET /api/users
 * - CUAD (User Add) -> POST /api/users
 * - CUUP (User Update) -> PUT /api/users/{userId}
 * - CUDL (User Delete) -> DELETE /api/users/{userId}
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth")
    @Operation(summary = "Authenticate user", description = "Replaces CICS transaction CC00 (Sign On)")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest) {
        boolean authenticated = userService.authenticate(authRequest.userId(), authRequest.password());
        
        if (authenticated) {
            User user = userService.findById(authRequest.userId()).orElseThrow();
            return ResponseEntity.ok(AuthResponse.success(
                    user.getUserId(),
                    user.getFullName(),
                    user.getUserType(),
                    "jwt-token-placeholder"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.failure("Invalid user ID or password"));
        }
    }

    @GetMapping
    @Operation(summary = "List all users", description = "Replaces CICS transaction CM00")
    public ResponseEntity<Page<UserDto>> listUsers(Pageable pageable) {
        Page<UserDto> users = userService.findAll(pageable).map(UserDto::from);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        return userService.findById(userId)
                .map(UserDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new user", description = "Replaces CICS transaction CUAD")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUserId(request.userId());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPassword(request.password());
        user.setUserType(request.userType());
        
        User created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.from(created));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user", description = "Replaces CICS transaction CUUP")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserRequest request) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUserType(request.userType());
        
        User updated = userService.update(user);
        return ResponseEntity.ok(UserDto.from(updated));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Replaces CICS transaction CUDL")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{userType}")
    @Operation(summary = "List users by type")
    public ResponseEntity<Page<UserDto>> listByUserType(@PathVariable UserType userType, Pageable pageable) {
        Page<UserDto> users = userService.findByUserType(userType, pageable).map(UserDto::from);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by name")
    public ResponseEntity<Page<UserDto>> searchByName(@RequestParam String name, Pageable pageable) {
        Page<UserDto> users = userService.searchByName(name, pageable).map(UserDto::from);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{userId}/change-password")
    @Operation(summary = "Change user password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String userId,
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request.oldPassword(), request.newPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/reset-password")
    @Operation(summary = "Reset user password (admin only)")
    public ResponseEntity<Void> resetPassword(
            @PathVariable String userId,
            @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(userId, request.newPassword());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/type")
    @Operation(summary = "Update user type")
    public ResponseEntity<UserDto> updateUserType(@PathVariable String userId, @RequestParam UserType userType) {
        User updated = userService.updateUserType(userId, userType);
        return ResponseEntity.ok(UserDto.from(updated));
    }

    @GetMapping("/count/admins")
    @Operation(summary = "Count admin users")
    public ResponseEntity<Long> countAdmins() {
        return ResponseEntity.ok(userService.countAdmins());
    }

    @GetMapping("/count/users")
    @Operation(summary = "Count regular users")
    public ResponseEntity<Long> countUsers() {
        return ResponseEntity.ok(userService.countUsers());
    }

    public record CreateUserRequest(
            String userId,
            String firstName,
            String lastName,
            String password,
            UserType userType
    ) {}

    public record UpdateUserRequest(
            String firstName,
            String lastName,
            UserType userType
    ) {}

    public record ChangePasswordRequest(
            String oldPassword,
            String newPassword
    ) {}

    public record ResetPasswordRequest(
            String newPassword
    ) {}
}
