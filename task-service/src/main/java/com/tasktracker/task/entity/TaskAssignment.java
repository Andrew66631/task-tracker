package com.tasktracker.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA-сущность записи о взятии задачи в работу.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_assignments", indexes = {
        @Index(name = "idx_assignments_task_id", columnList = "task_id"),
        @Index(name = "idx_assignments_assignee_id", columnList = "assignee_id"),
        @Index(name = "idx_assignments_reviewer_id", columnList = "reviewer_id"),
        @Index(name = "idx_assignments_tester_id", columnList = "tester_id")
})
public class TaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Column(name = "assignee_id")
    private UUID assigneeId;

    @Column(name = "reviewer_id")
    private UUID reviewerId;

    @Column(name = "tester_id")
    private UUID testerId;

    @Column(name = "taken_at", nullable = false)
    private LocalDateTime takenAt;

    @Column(name = "deployed_at")
    private LocalDateTime deployedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.takenAt == null) {
            this.takenAt = LocalDateTime.now();
        }
    }
}