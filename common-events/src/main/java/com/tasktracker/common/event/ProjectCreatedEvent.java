package com.tasktracker.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectCreatedEvent(
        UUID projectId,
        String name,
        String description,
        UUID ownerId,
        UUID organizationId,
        LocalDateTime createdAt
) {}