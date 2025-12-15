package com.carddemo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Authentication request DTO - replaces COBOL COSGN00C.cbl sign-on screen input.
 */
public record AuthRequest(
        @NotBlank @Size(max = 8) String userId,
        @NotBlank @Size(max = 8) String password
) {
}
