package com.tasktracker.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCreatedEvent(
        UUID userId,
        String username,
        String email,
        String fullName,
        String role,
        UUID organizationId,
        LocalDateTime createdAt
) {}