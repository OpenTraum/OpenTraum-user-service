package com.opentraum.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tenants")
public class Tenant {

    @Id
    private Long id;

    private String name;

    private String slug;

    private String domain;

    private String plan;

    @Column("is_active")
    private Boolean isActive;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
}
