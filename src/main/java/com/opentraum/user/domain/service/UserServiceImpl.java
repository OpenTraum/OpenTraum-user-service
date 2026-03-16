package com.opentraum.user.domain.service;

import com.opentraum.user.domain.dto.UserCreateRequest;
import com.opentraum.user.domain.dto.UserResponse;
import com.opentraum.user.domain.entity.User;
import com.opentraum.user.domain.repository.UserRepository;
import com.opentraum.user.global.exception.BusinessException;
import com.opentraum.user.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Mono<UserResponse> createUser(UserCreateRequest request) {
        return userRepository.existsByEmail(request.email())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.<User>error(new BusinessException(ErrorCode.DUPLICATE_EMAIL));
                    }
                    User user = User.builder()
                            .email(request.email())
                            .password(request.password())
                            .name(request.name())
                            .phone(request.phone())
                            .role(request.role() != null ? request.role() : "USER")
                            .tenantId(request.tenantId())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return userRepository.save(user);
                })
                .map(UserResponse::from);
    }

    @Override
    public Mono<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)))
                .map(UserResponse::from);
    }

    @Override
    public Mono<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)))
                .map(UserResponse::from);
    }

    @Override
    public Flux<UserResponse> getUsersByTenantId(Long tenantId) {
        return userRepository.findByTenantId(tenantId)
                .map(UserResponse::from);
    }

    @Override
    public Flux<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .map(UserResponse::from);
    }

    @Override
    @Transactional
    public Mono<UserResponse> updateUser(Long id, UserCreateRequest request) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(existingUser -> {
                    existingUser.setEmail(request.email());
                    existingUser.setName(request.name());
                    existingUser.setPhone(request.phone());
                    existingUser.setRole(request.role() != null ? request.role() : existingUser.getRole());
                    existingUser.setTenantId(request.tenantId());
                    existingUser.setUpdatedAt(LocalDateTime.now());
                    if (request.password() != null && !request.password().isBlank()) {
                        existingUser.setPassword(request.password());
                    }
                    return userRepository.save(existingUser);
                })
                .map(UserResponse::from);
    }

    @Override
    @Transactional
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(userRepository::delete);
    }
}
