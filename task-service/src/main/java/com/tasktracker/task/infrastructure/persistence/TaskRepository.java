package com.tasktracker.task.infrastructure.persistence;

import com.tasktracker.task.domain.enums.TaskPriority;
import com.tasktracker.task.domain.enums.TaskStatus;
import com.tasktracker.task.domain.enums.TaskType;
import com.tasktracker.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByTaskType(TaskType taskType);

    /**
     * Найти все задачи в проекте.
     */
    List<Task> findByProjectId(UUID projectId);   // ← ИЗМЕНЕНО

    List<Task> findByCreatedBy(UUID createdBy);

    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);

    /**
     * Найти все задачи в проекте с определённым статусом.
     */
    List<Task> findByProjectIdAndStatus(UUID projectId, TaskStatus status);   // ← ИЗМЕНЕНО

    /**
     * Проверить, существует ли задача с таким заголовком в проекте.
     */
    boolean existsByProjectIdAndTitle(UUID projectId, String title);   // ← ИЗМЕНЕНО

    long countByStatus(TaskStatus status);

    long countByCreatedBy(UUID createdBy);
}