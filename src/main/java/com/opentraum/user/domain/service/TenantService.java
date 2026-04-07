package com.opentraum.user.domain.service;

import com.opentraum.user.domain.dto.TenantCreateRequest;
import com.opentraum.user.domain.dto.TenantResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TenantService {

    Mono<TenantResponse> createTenant(TenantCreateRequest request);

    Mono<TenantResponse> getTenantById(Long id);

    Mono<TenantResponse> getTenantBySlug(String slug);

    Flux<TenantResponse> getAllTenants();

    Mono<TenantResponse> updateTenant(Long id, TenantCreateRequest request);

    Mono<TenantResponse> deactivateTenant(Long id);

    Mono<Void> deleteTenant(Long id);
}
