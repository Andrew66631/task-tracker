package com.tasktracker.task.kafka;

import com.tasktracker.common.event.ProjectCreatedEvent;
import com.tasktracker.task.entity.ProjectProjection;
import com.tasktracker.task.infrastructure.persistence.ProjectProjectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectEventListener {

    private final ProjectProjectionRepository projectProjectionRepository;

    @KafkaListener(
            topics = "project.created",
            groupId = "task-service",
            containerFactory = "projectCreatedKafkaListenerContainerFactory"
    )
    @Transactional
    public void onProjectCreated(ProjectCreatedEvent event) {
        log.info("Received ProjectCreatedEvent: {}", event);

        ProjectProjection projection = ProjectProjection.builder()
                .projectId(event.projectId())
                .name(event.name())
                .description(event.description())
                .ownerId(event.ownerId())
                .organizationId(event.organizationId())
                .createdAt(event.createdAt())
                .build();

        projectProjectionRepository.save(projection);
        log.info("Saved project projection: {}", event.projectId());
    }
}