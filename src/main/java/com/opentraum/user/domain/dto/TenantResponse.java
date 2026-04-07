package com.opentraum.user.domain.dto;

import com.opentraum.user.domain.entity.Tenant;

import java.time.LocalDateTime;

public record TenantResponse(
        Long id,
        String name,
        String slug,
        String domain,
        String plan,
        Boolean isActive,
        LocalDateTime createdAt
) {
    public static TenantResponse from(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getSlug(),
                tenant.getDomain(),
                tenant.getPlan(),
                tenant.getIsActive(),
                tenant.getCreatedAt()
        );
    }
}
