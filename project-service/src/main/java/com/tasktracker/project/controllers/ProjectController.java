package com.tasktracker.project.controllers;

import com.tasktracker.project.dto.CreateProjectRequest;
import com.tasktracker.project.dto.ProjectResponse;
import com.tasktracker.project.dto.UpdateProjectRequest;
import com.tasktracker.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(
            @RequestParam(required = false) UUID ownerId) {
        if (ownerId != null) {
            return ResponseEntity.ok(projectService.getProjectsByOwner(ownerId));
        }
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZATION', 'ADMIN')")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(request, authentication));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZATION', 'ADMIN')")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(projectService.updateProject(id, request, authentication));
    }

    @PutMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('ORGANIZATION', 'ADMIN')")
    public ResponseEntity<ProjectResponse> archiveProject(
            @PathVariable UUID id,
            Authentication authentication) {
        return ResponseEntity.ok(projectService.archiveProject(id, authentication));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZATION', 'ADMIN')")
    public ResponseEntity<Void> deleteProject(
            @PathVariable UUID id,
            Authentication authentication) {
        projectService.deleteProject(id, authentication);
        return ResponseEntity.noContent().build();
    }
}