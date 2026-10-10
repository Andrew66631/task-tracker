package com.tasktracker.task.application;

import com.tasktracker.task.domain.enums.TaskStatus;
import com.tasktracker.task.entity.Task;
import com.tasktracker.task.exception.TaskNotFoundException;
import com.tasktracker.task.infrastructure.persistence.TaskRepository;
import com.tasktracker.task.infrastructure.web.dto.CreateTaskRequest;
import com.tasktracker.task.infrastructure.web.dto.TaskResponse;
import com.tasktracker.task.infrastructure.web.dto.UpdateTaskRequest;
import com.tasktracker.task.infrastructure.web.mapper.TaskMapper;
import com.tasktracker.task.kafka.TaskEventPublisher;   // ← НОВОЕ
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;   // ← НОВОЕ

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

        // ✅ Публикуем событие через outbox
        taskEventPublisher.publishTaskCreated(saved);

        return TaskMapper.INSTANCE.toResponse(saved);
    }

    @Transactional
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

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
        if (request.getProjectId() != null) {           // ← ИЗМЕНЕНО
            task.setProjectId(request.getProjectId());  // ← ИЗМЕНЕНО
        }

        Task updated = taskRepository.save(task);
        return TaskMapper.INSTANCE.toResponse(updated);
    }

    @Transactional
    public TaskResponse changeStatus(UUID id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

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