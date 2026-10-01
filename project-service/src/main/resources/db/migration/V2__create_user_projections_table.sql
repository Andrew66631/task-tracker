CREATE TABLE user_projections (
                                  user_id         UUID PRIMARY KEY,
                                  username        VARCHAR(100) NOT NULL,
                                  email           VARCHAR(255) NOT NULL,
                                  full_name       VARCHAR(255),
                                  role            VARCHAR(50) NOT NULL,
                                  organization_id UUID,
                                  created_at      TIMESTAMP NOT NULL,
                                  updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_user_projections_organization_id ON user_projections(organization_id);
CREATE INDEX idx_user_projections_role ON user_projections(role);