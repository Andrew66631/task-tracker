package com.tasktracker.task.kafka;

import com.tasktracker.common.event.TaskCreatedEvent;
import com.tasktracker.task.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventPublisher {

    private final OutboxService outboxService;

    public void publishTaskCreated(Task task) {
        TaskCreatedEvent event = new TaskCreatedEvent(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus() != null ? task.getStatus().name() : null,
                task.getPriority() != null ? task.getPriority().name() : null,
                task.getTaskType() != null ? task.getTaskType().name() : null,
                task.getProjectId(),   // ← ТЕПЕРЬ РАБОТАЕТ
                task.getCreatedBy(),
                task.getCreatedAt()
        );

        outboxService.saveEvent(
                task.getId(),
                "Task",
                "task.created",
                event
        );

        log.info("TaskCreatedEvent saved to outbox: {}", task.getId());
    }
}