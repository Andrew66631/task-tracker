package com.tasktracker.task.application;

import com.tasktracker.task.domain.enums.TaskStatus;
import com.tasktracker.task.entity.Task;
import com.tasktracker.task.exception.TaskNotFoundException;
import com.tasktracker.task.infrastructure.persistence.TaskRepository;
import com.tasktracker.task.infrastructure.web.dto.CreateTaskRequest;
import com.tasktracker.task.infrastructure.web.dto.TaskResponse;
import com.tasktracker.task.infrastructure.web.dto.UpdateTaskRequest;
import com.tasktracker.task.infrastructure.web.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.INSTANCE.toResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(TaskMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = TaskMapper.INSTANCE.toEntity(request);
        Task saved = taskRepository.save(task);
        return TaskMapper.INSTANCE.toResponse(saved);
    }

    @Transactional
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // ✅ Обновляем ТОЛЬКО те поля, которые есть в UpdateTaskRequest
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getTaskType() != null) {
            task.setTaskType(request.getTaskType());
        }
        if (request.getGroupId() != null) {
            task.setGroupId(request.getGroupId());
        }
        // ❌ Строка с request.getStatus() УДАЛЕНА — статус меняется отдельно

        Task updated = taskRepository.save(task);
        return TaskMapper.INSTANCE.toResponse(updated);
    }

    /**
     * Смена статуса — отдельная операция.
     */
    @Transactional
    public TaskResponse changeStatus(UUID id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // TODO: здесь можно добавить проверку переходов статусов
        task.setStatus(newStatus);

        Task updated = taskRepository.save(task);
        return TaskMapper.INSTANCE.toResponse(updated);
    }

    @Transactional
    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}