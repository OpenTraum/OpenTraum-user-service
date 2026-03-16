package com.opentraum.user.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력입니다"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 오류가 발생했습니다"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U002", "이미 사용 중인 이메일입니다"),

    // Tenant
    TENANT_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "테넌트를 찾을 수 없습니다"),
    DUPLICATE_SLUG(HttpStatus.CONFLICT, "T002", "이미 사용 중인 슬러그입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
