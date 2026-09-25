-- Таблица пользователей
CREATE TABLE users (
                       id                  UUID PRIMARY KEY,
                       username            VARCHAR(100) NOT NULL UNIQUE,
                       email               VARCHAR(255) NOT NULL UNIQUE,
                       password_hash       VARCHAR(255) NOT NULL,
                       full_name           VARCHAR(255),
                       role                VARCHAR(50) NOT NULL DEFAULT 'USER',
                       sub_role            VARCHAR(50),
                       status              VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                       organization_id     UUID,
                       created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
                       approved_at         TIMESTAMP,
                       approved_by         UUID
);

-- Индексы
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_organization_id ON users(organization_id);