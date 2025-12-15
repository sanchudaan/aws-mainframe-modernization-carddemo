package com.carddemo.api.dto;

import com.carddemo.common.constant.UserType;

/**
 * Authentication response DTO - replaces COBOL COSGN00C.cbl sign-on response.
 */
public record AuthResponse(
        String userId,
        String fullName,
        UserType userType,
        boolean authenticated,
        String message,
        String token
) {
    public static AuthResponse success(String userId, String fullName, UserType userType, String token) {
        return new AuthResponse(userId, fullName, userType, true, "Authentication successful", token);
    }

    public static AuthResponse failure(String message) {
        return new AuthResponse(null, null, null, false, message, null);
    }
}
