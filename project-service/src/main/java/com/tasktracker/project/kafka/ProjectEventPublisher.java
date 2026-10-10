package com.tasktracker.project.kafka;

import com.tasktracker.common.event.ProjectCreatedEvent;
import com.tasktracker.project.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectEventPublisher {

    private final OutboxService outboxService;

    public void publishProjectCreated(Project project) {
        ProjectCreatedEvent event = new ProjectCreatedEvent(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwnerId(),
                project.getOrganizationId(),
                project.getCreatedAt()
        );

        outboxService.saveEvent(
                project.getId(),
                "Project",
                "project.created",
                event
        );

        log.info("ProjectCreatedEvent saved to outbox: {}", project.getId());
    }
}