package com.tasktracker.task.infrastructure.web.dto;

import com.tasktracker.task.domain.enums.TaskPriority;
import com.tasktracker.task.domain.enums.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO для создания новой задачи.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "Название задачи обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @Size(max = 5000, message = "Описание не должно превышать 5000 символов")
    private String description;

    @NotNull(message = "Приоритет обязателен")
    private TaskPriority priority;

    @NotNull(message = "Тип задачи обязателен")
    private TaskType taskType;

    private UUID groupId;

    @NotNull(message = "ID автора обязателен")
    private UUID createdBy;
}