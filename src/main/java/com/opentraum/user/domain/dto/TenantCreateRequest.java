package com.opentraum.user.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record TenantCreateRequest(
        @NotBlank(message = "테넌트 이름은 필수입니다")
        String name,

        @NotBlank(message = "슬러그는 필수입니다")
        String slug,

        String domain,

        String plan
) {}
