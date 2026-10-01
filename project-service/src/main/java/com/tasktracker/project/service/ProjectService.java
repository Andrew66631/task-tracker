package com.tasktracker.project.service;

import com.tasktracker.project.dto.CreateProjectRequest;
import com.tasktracker.project.dto.ProjectResponse;
import com.tasktracker.project.dto.UpdateProjectRequest;
import com.tasktracker.project.entity.Project;
import com.tasktracker.project.entity.enums.ProjectStatus;
import com.tasktracker.project.exception.ProjectAccessDeniedException;
import com.tasktracker.project.exception.ProjectNotFoundException;
import com.tasktracker.project.mapper.ProjectMapper;
import com.tasktracker.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectMapper.INSTANCE.toResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByOwner(UUID ownerId) {
        return projectRepository.findByOwnerId(ownerId).stream()
                .map(ProjectMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request, Authentication authentication) {
        UUID ownerId = getCurrentUserId(authentication);

        Project project = ProjectMapper.INSTANCE.toEntity(request);
        project.setOwnerId(ownerId);

        Project saved = projectRepository.save(project);
        return ProjectMapper.INSTANCE.toResponse(saved);
    }

    @Transactional
    public ProjectResponse updateProject(UUID id, UpdateProjectRequest request, Authentication authentication) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        checkAccess(project, authentication);

        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());

        return ProjectMapper.INSTANCE.toResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse archiveProject(UUID id, Authentication authentication) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        checkAccess(project, authentication);

        project.setStatus(ProjectStatus.ARCHIVED);
        return ProjectMapper.INSTANCE.toResponse(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(UUID id, Authentication authentication) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        checkAccess(project, authentication);

        projectRepository.delete(project);
    }

    private void checkAccess(Project project, Authentication authentication) {
        UUID currentUserId = getCurrentUserId(authentication);
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));

        if (!isAdmin && !project.getOwnerId().equals(currentUserId)) {
            throw new ProjectAccessDeniedException("You don't have access to this project");
        }
    }

    private UUID getCurrentUserId(Authentication authentication) {
        return UUID.fromString((String) authentication.getPrincipal());
    }
}