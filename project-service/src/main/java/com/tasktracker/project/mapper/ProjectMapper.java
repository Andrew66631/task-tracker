package com.tasktracker.project.mapper;

import com.tasktracker.project.dto.CreateProjectRequest;
import com.tasktracker.project.dto.ProjectResponse;
import com.tasktracker.project.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = "statusName", expression = "java(project.getStatus().getDisplayName())")
    ProjectResponse toResponse(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Project toEntity(CreateProjectRequest request);
}