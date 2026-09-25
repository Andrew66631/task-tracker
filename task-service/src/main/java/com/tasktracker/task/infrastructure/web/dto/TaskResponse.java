package com.tasktracker.task.infrastructure.web.dto;

import com.tasktracker.task.domain.enums.TaskPriority;
import com.tasktracker.task.domain.enums.TaskStatus;
import com.tasktracker.task.domain.enums.TaskType;
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
public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private String statusName;
    private TaskPriority priority;
    private String priorityName;
    private TaskType taskType;
    private String taskTypeName;
    private UUID groupId;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}