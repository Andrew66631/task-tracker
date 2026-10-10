ALTER TABLE tasks RENAME COLUMN group_id TO project_id;
ALTER INDEX idx_tasks_group_id RENAME TO idx_tasks_project_id;