CREATE INDEX IF NOT EXISTS idx_projects_outbox_status ON outbox(status);
CREATE INDEX IF NOT EXISTS idx_projects_outbox_created_at ON outbox(created_at);