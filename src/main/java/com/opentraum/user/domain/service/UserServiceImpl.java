package com.opentraum.user.domain.service;

import com.opentraum.user.domain.dto.UserCreateRequest;
import com.opentraum.user.domain.dto.UserResponse;
import com.opentraum.user.domain.entity.User;
import com.opentraum.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                        return Mono.error(new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.email()));
                    }
                    User user = User.builder()
                            .email(request.email())
                            .password(request.password())
                            .name(request.name())
                            .role(request.role() != null ? request.role() : "USER")
                            .tenantId(request.tenantId())
                            .build();
                    return userRepository.save(user);
                })
                .map(UserResponse::from);
    }

    @Override
    public Mono<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id)))
                .map(UserResponse::from);
    }

    @Override
    public Mono<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email)))
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
                .switchIfEmpty(Mono.error(new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id)))
                .flatMap(existingUser -> {
                    existingUser.setEmail(request.email());
                    existingUser.setName(request.name());
                    existingUser.setRole(request.role() != null ? request.role() : existingUser.getRole());
                    existingUser.setTenantId(request.tenantId());
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
                .switchIfEmpty(Mono.error(new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id)))
                .flatMap(userRepository::delete);
    }
}
