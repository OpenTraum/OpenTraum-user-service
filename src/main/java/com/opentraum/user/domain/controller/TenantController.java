package com.opentraum.user.domain.controller;

import com.opentraum.user.domain.dto.TenantCreateRequest;
import com.opentraum.user.domain.dto.TenantResponse;
import com.opentraum.user.domain.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TenantResponse> createTenant(@Valid @RequestBody TenantCreateRequest request) {
        return tenantService.createTenant(request);
    }

    @GetMapping("/{id}")
    public Mono<TenantResponse> getTenantById(@PathVariable Long id) {
        return tenantService.getTenantById(id);
    }

    @GetMapping("/slug/{slug}")
    public Mono<TenantResponse> getTenantBySlug(@PathVariable String slug) {
        return tenantService.getTenantBySlug(slug);
    }

    @GetMapping
    public Flux<TenantResponse> getAllTenants() {
        return tenantService.getAllTenants();
    }

    @PutMapping("/{id}")
    public Mono<TenantResponse> updateTenant(
            @PathVariable Long id,
            @Valid @RequestBody TenantCreateRequest request) {
        return tenantService.updateTenant(id, request);
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<TenantResponse> deactivateTenant(@PathVariable Long id) {
        return tenantService.deactivateTenant(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTenant(@PathVariable Long id) {
        return tenantService.deleteTenant(id);
    }
}
