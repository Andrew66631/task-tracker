package com.tasktracker.task.infrastructure.persistence;

import com.tasktracker.task.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с записями о взятии задач в работу.
 */
@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, UUID> {

    /**
     * Найти все записи по ID задачи.
     */
    List<TaskAssignment> findByTaskId(UUID taskId);

    /**
     * Найти все задачи, назначенные на исполнителя.
     */
    List<TaskAssignment> findByAssigneeId(UUID assigneeId);

    /**
     * Найти все задачи, назначенные на ревьюера.
     */
    List<TaskAssignment> findByReviewerId(UUID reviewerId);

    /**
     * Найти все задачи, назначенные на тестировщика.
     */
    List<TaskAssignment> findByTesterId(UUID testerId);

    /**
     * Найти активную запись по задаче (без deployedAt).
     */
    Optional<TaskAssignment> findByTaskIdAndDeployedAtIsNull(UUID taskId);

    /**
     * Проверить, существует ли активная запись по задаче.
     */
    boolean existsByTaskIdAndDeployedAtIsNull(UUID taskId);

    /**
     * Посчитать количество активных задач у исполнителя.
     */
    long countByAssigneeIdAndDeployedAtIsNull(UUID assigneeId);
}