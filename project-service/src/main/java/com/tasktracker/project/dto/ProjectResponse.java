package com.tasktracker.project.dto;

import com.tasktracker.project.entity.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private UUID id;
    private String name;
    private String description;
    private UUID ownerId;
    private UUID organizationId;
    private ProjectStatus status;
    private String statusName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}