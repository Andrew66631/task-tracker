package com.tasktracker.task.infrastructure.web.dto;

import com.tasktracker.task.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для смены статуса задачи.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusRequest {

    @NotNull(message = "Новый статус обязателен")
    private TaskStatus newStatus;
}