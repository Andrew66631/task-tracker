package com.tasktracker.project.repository;

import com.tasktracker.project.entity.Project;
import com.tasktracker.project.entity.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByOwnerId(UUID ownerId);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByOwnerIdAndStatus(UUID ownerId, ProjectStatus status);

    List<Project> findByOrganizationId(UUID organizationId);

    boolean existsByNameAndOwnerId(String name, UUID ownerId);
}