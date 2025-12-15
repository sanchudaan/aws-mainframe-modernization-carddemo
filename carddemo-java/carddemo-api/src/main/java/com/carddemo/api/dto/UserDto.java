package com.carddemo.api.dto;

import com.carddemo.common.constant.UserType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for User entity.
 * Used for REST API request/response payloads.
 */
public record UserDto(
        @Size(max = 8) String userId,
        @NotNull @Size(max = 20) String firstName,
        @NotNull @Size(max = 20) String lastName,
        String fullName,
        @NotNull UserType userType,
        boolean isAdmin,
        String lastUpdateDate,
        String lastUpdateTime
) {
    public static UserDto from(com.carddemo.core.user.entity.User user) {
        return new UserDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getUserType(),
                user.isAdmin(),
                user.getLastUpdateDate(),
                user.getLastUpdateTime()
        );
    }

    public com.carddemo.core.user.entity.User toEntity() {
        com.carddemo.core.user.entity.User user = new com.carddemo.core.user.entity.User();
        user.setUserId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserType(userType);
        return user;
    }
}
