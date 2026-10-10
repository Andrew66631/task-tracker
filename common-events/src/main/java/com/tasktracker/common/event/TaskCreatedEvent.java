package com.tasktracker.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCreatedEvent(
        UUID taskId,
        String title,
        String description,
        String status,
        String priority,
        String taskType,
        UUID projectId,
        UUID authorId,
        LocalDateTime createdAt
) {}