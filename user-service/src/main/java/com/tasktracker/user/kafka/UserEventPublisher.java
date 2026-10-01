package com.tasktracker.user.kafka;

import com.tasktracker.common.event.UserCreatedEvent;
import com.tasktracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final OutboxService outboxService;

    public void publishUserCreated(User user) {
        UserCreatedEvent event = new UserCreatedEvent(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                user.getOrganizationId(),
                user.getCreatedAt()
        );

        outboxService.saveEvent(
                user.getId(),
                "User",
                "user.created",
                event
        );

        log.info("UserCreatedEvent saved to outbox: {}", user.getId());
    }
}