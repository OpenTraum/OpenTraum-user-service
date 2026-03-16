package com.opentraum.user.domain.repository;

import com.opentraum.user.domain.entity.User;
import org.springframework.data.r2dbc.repository.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByEmail(String email);

    Flux<User> findByTenantId(Long tenantId);

    Mono<Boolean> existsByEmail(String email);
}
