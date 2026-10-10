-- Проекция пользователя (своя для task-service)
CREATE TABLE IF NOT EXISTS task_user_projections (
                                                     user_id         UUID PRIMARY KEY,
                                                     username        VARCHAR(100) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255),
    role            VARCHAR(50) NOT NULL,
    organization_id UUID,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
    );

-- Проекция проекта (своя для task-service)
CREATE TABLE IF NOT EXISTS task_project_projections (
                                                        project_id      UUID PRIMARY KEY,
                                                        name            VARCHAR(255) NOT NULL,
    description     TEXT,
    owner_id        UUID NOT NULL,
    organization_id UUID,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
    );

-- Индексы
CREATE INDEX IF NOT EXISTS idx_task_user_proj_org ON task_user_projections(organization_id);
CREATE INDEX IF NOT EXISTS idx_task_project_proj_owner ON task_project_projections(owner_id);
CREATE INDEX IF NOT EXISTS idx_task_project_proj_org ON task_project_projections(organization_id);