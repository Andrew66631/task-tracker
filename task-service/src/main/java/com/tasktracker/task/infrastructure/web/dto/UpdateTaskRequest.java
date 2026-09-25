package com.tasktracker.task.infrastructure.web.dto;

import com.tasktracker.task.domain.enums.TaskPriority;
import com.tasktracker.task.domain.enums.TaskType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO для обновления задачи.
 * Все поля опциональны — обновляются только те, что не null.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {

    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @Size(max = 5000, message = "Описание не должно превышать 5000 символов")
    private String description;

    private TaskPriority priority;

    private TaskType taskType;

    private UUID groupId;
}