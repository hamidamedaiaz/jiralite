package com.jiralite.dto;

public record UserResponse(
        long id,
        String username,
        String email,
        String role,
        String createdAt
) {
}
