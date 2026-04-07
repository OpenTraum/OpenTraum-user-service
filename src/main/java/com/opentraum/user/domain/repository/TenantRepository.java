package com.opentraum.user.domain.repository;

import com.opentraum.user.domain.entity.Tenant;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TenantRepository extends ReactiveCrudRepository<Tenant, Long> {

    Mono<Tenant> findBySlug(String slug);

    Mono<Tenant> findByDomain(String domain);

    Mono<Boolean> existsBySlug(String slug);
}
