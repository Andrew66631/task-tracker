package com.tasktracker.task.kafka;

import com.tasktracker.common.event.UserCreatedEvent;
import com.tasktracker.task.entity.UserProjection;
import com.tasktracker.task.infrastructure.persistence.UserProjectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserProjectionRepository userProjectionRepository;

    @KafkaListener(
            topics = "user.created",
            groupId = "task-service",
            containerFactory = "userCreatedKafkaListenerContainerFactory"
    )
    @Transactional
    public void onUserCreated(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent: {}", event);

        UserProjection projection = UserProjection.builder()
                .userId(event.userId())
                .username(event.username())
                .email(event.email())
                .fullName(event.fullName())
                .role(event.role())
                .organizationId(event.organizationId())
                .createdAt(event.createdAt())
                .build();

        userProjectionRepository.save(projection);
        log.info("Saved user projection: {}", event.userId());
    }
}