package com.tasktracker.task.infrastructure.web.mapper;

import com.tasktracker.task.entity.Task;
import com.tasktracker.task.infrastructure.web.dto.CreateTaskRequest;
import com.tasktracker.task.infrastructure.web.dto.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "statusName", expression = "java(task.getStatus().getDisplayName())")
    @Mapping(target = "priorityName", expression = "java(task.getPriority().getDisplayName())")
    @Mapping(target = "taskTypeName", expression = "java(task.getTaskType().getDisplayName())")
    TaskResponse toResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Task toEntity(CreateTaskRequest request);
}