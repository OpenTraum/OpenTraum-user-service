package com.opentraum.user.domain.service;

import com.opentraum.user.domain.dto.TenantCreateRequest;
import com.opentraum.user.domain.dto.TenantResponse;
import com.opentraum.user.domain.entity.Tenant;
import com.opentraum.user.domain.repository.TenantRepository;
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
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public Mono<TenantResponse> createTenant(TenantCreateRequest request) {
        return tenantRepository.existsBySlug(request.slug())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.<Tenant>error(new BusinessException(ErrorCode.DUPLICATE_SLUG));
                    }
                    Tenant tenant = Tenant.builder()
                            .name(request.name())
                            .slug(request.slug())
                            .domain(request.domain())
                            .plan(request.plan() != null ? request.plan() : "FREE")
                            .isActive(true)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return tenantRepository.save(tenant);
                })
                .map(TenantResponse::from);
    }

    @Override
    public Mono<TenantResponse> getTenantById(Long id) {
        return tenantRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.TENANT_NOT_FOUND)))
                .map(TenantResponse::from);
    }

    @Override
    public Mono<TenantResponse> getTenantBySlug(String slug) {
        return tenantRepository.findBySlug(slug)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.TENANT_NOT_FOUND)))
                .map(TenantResponse::from);
    }

    @Override
    public Flux<TenantResponse> getAllTenants() {
        return tenantRepository.findAll()
                .map(TenantResponse::from);
    }

    @Override
    @Transactional
    public Mono<TenantResponse> updateTenant(Long id, TenantCreateRequest request) {
        return tenantRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.TENANT_NOT_FOUND)))
                .flatMap(existingTenant -> {
                    existingTenant.setName(request.name());
                    existingTenant.setSlug(request.slug());
                    existingTenant.setDomain(request.domain());
                    if (request.plan() != null) {
                        existingTenant.setPlan(request.plan());
                    }
                    return tenantRepository.save(existingTenant);
                })
                .map(TenantResponse::from);
    }

    @Override
    @Transactional
    public Mono<TenantResponse> deactivateTenant(Long id) {
        return tenantRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.TENANT_NOT_FOUND)))
                .flatMap(tenant -> {
                    tenant.setIsActive(false);
                    return tenantRepository.save(tenant);
                })
                .map(TenantResponse::from);
    }

    @Override
    @Transactional
    public Mono<Void> deleteTenant(Long id) {
        return tenantRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.TENANT_NOT_FOUND)))
                .flatMap(tenantRepository::delete);
    }
}
