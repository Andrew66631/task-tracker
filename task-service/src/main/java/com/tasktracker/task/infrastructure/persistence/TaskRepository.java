package com.tasktracker.task.infrastructure.persistence;

import com.tasktracker.task.domain.enums.TaskPriority;
import com.tasktracker.task.domain.enums.TaskStatus;
import com.tasktracker.task.domain.enums.TaskType;
import com.tasktracker.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с задачами.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    /**
     * Найти все задачи с определённым статусом.
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Найти все задачи с определённым приоритетом.
     */
    List<Task> findByPriority(TaskPriority priority);

    /**
     * Найти все задачи с определённым типом.
     */
    List<Task> findByTaskType(TaskType taskType);

    /**
     * Найти все задачи в группе.
     */
    List<Task> findByGroupId(UUID groupId);

    /**
     * Найти все задачи, созданные пользователем.
     */
    List<Task> findByCreatedBy(UUID createdBy);

    /**
     * Найти все задачи с определённым статусом и приоритетом.
     */
    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);

    /**
     * Найти все задачи в группе с определённым статусом.
     */
    List<Task> findByGroupIdAndStatus(UUID groupId, TaskStatus status);

    /**
     * Проверить, существует ли задача с таким заголовком в группе.
     */
    boolean existsByGroupIdAndTitle(UUID groupId, String title);

    /**
     * Посчитать количество задач с определённым статусом.
     */
    long countByStatus(TaskStatus status);

    /**
     * Посчитать количество задач, созданных пользователем.
     */
    long countByCreatedBy(UUID createdBy);
}