package com.opentraum.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "이메일 형식이 올바르지 않습니다")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, max = 50, message = "비밀번호는 8~50자여야 합니다")
        String password,

        @NotBlank(message = "이름은 필수입니다")
        String name,

        String phone,

        String role,

        @NotNull(message = "테넌트 ID는 필수입니다")
        Long tenantId
) {}
