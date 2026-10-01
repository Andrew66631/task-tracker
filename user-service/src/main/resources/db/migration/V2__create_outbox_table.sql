CREATE TABLE outbox (
                        id              UUID PRIMARY KEY,
                        aggregate_id    UUID NOT NULL,
                        aggregate_type  VARCHAR(100) NOT NULL,
                        event_type      VARCHAR(100) NOT NULL,
                        payload         TEXT NOT NULL,
                        created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                        sent_at         TIMESTAMP,
                        status          VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

CREATE INDEX idx_outbox_status ON outbox(status);
CREATE INDEX idx_outbox_created_at ON outbox(created_at);