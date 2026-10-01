CREATE TABLE projects (
                          id              UUID PRIMARY KEY,
                          name            VARCHAR(255) NOT NULL,
                          description     TEXT,
                          owner_id        UUID NOT NULL,
                          organization_id UUID,
                          status          VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                          created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                          version         BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_projects_owner_id ON projects(owner_id);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_organization_id ON projects(organization_id);