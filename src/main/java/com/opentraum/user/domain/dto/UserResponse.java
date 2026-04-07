package com.opentraum.user.domain.dto;

import com.opentraum.user.domain.entity.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String name,
        String phone,
        String role,
        Long tenantId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getRole(),
                user.getTenantId(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
