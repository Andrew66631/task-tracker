CREATE INDEX IF NOT EXISTS idx_task_outbox_status ON outbox(status);
CREATE INDEX IF NOT EXISTS idx_task_outbox_created_at ON outbox(created_at);