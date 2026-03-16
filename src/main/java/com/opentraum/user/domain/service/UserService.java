package com.opentraum.user.domain.service;

import com.opentraum.user.domain.dto.UserCreateRequest;
import com.opentraum.user.domain.dto.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserResponse> createUser(UserCreateRequest request);

    Mono<UserResponse> getUserById(Long id);

    Mono<UserResponse> getUserByEmail(String email);

    Flux<UserResponse> getUsersByTenantId(Long tenantId);

    Flux<UserResponse> getAllUsers();

    Mono<UserResponse> updateUser(Long id, UserCreateRequest request);

    Mono<Void> deleteUser(Long id);
}
