-- ============================================
-- Таблица задач
-- ============================================
CREATE TABLE tasks (
                       id              UUID PRIMARY KEY,
                       title           VARCHAR(255) NOT NULL,
                       description     TEXT,
                       status          VARCHAR(50) NOT NULL DEFAULT 'NEW',
                       priority        VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
                       task_type       VARCHAR(50) NOT NULL DEFAULT 'DEVELOPMENT',
                       group_id        UUID,
                       created_by      UUID NOT NULL,
                       created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                       version         BIGINT NOT NULL DEFAULT 0
);

-- ============================================
-- Таблица взятых задач
-- ============================================
CREATE TABLE task_assignments (
                                  id              UUID PRIMARY KEY,
                                  task_id         UUID NOT NULL,
                                  assignee_id     UUID,
                                  reviewer_id     UUID,
                                  tester_id       UUID,
                                  taken_at        TIMESTAMP NOT NULL DEFAULT NOW(),
                                  deployed_at     TIMESTAMP,
                                  created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================
-- Индексы для таблицы tasks
-- ============================================
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_priority ON tasks(priority);
CREATE INDEX idx_tasks_group_id ON tasks(group_id);
CREATE INDEX idx_tasks_created_by ON tasks(created_by);

-- ============================================
-- Индексы для таблицы task_assignments
-- ============================================
CREATE INDEX idx_assignments_task_id ON task_assignments(task_id);
CREATE INDEX idx_assignments_assignee_id ON task_assignments(assignee_id);
CREATE INDEX idx_assignments_reviewer_id ON task_assignments(reviewer_id);
CREATE INDEX idx_assignments_tester_id ON task_assignments(tester_id);