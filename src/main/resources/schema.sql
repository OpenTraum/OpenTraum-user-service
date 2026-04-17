CREATE TABLE IF NOT EXISTS tenants (
    id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    slug        VARCHAR(100)    NOT NULL UNIQUE,
    domain      VARCHAR(255),
    plan        VARCHAR(50)     NOT NULL DEFAULT 'FREE',
    is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    INDEX idx_tenants_slug (slug),
    INDEX idx_tenants_domain (domain)
);

CREATE TABLE IF NOT EXISTS users (
    id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(255)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    name        VARCHAR(100)    NOT NULL,
    phone       VARCHAR(20),
    role        VARCHAR(50)     NOT NULL DEFAULT 'USER',
    tenant_id   BIGINT          NOT NULL,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    INDEX idx_users_email (email),
    INDEX idx_users_tenant_id (tenant_id),
    FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE
);
