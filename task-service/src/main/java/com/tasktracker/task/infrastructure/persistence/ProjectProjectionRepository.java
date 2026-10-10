package com.tasktracker.task.infrastructure.persistence;

import com.tasktracker.task.entity.ProjectProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectProjectionRepository extends JpaRepository<ProjectProjection, UUID> {

    List<ProjectProjection> findByOwnerId(UUID ownerId);

    List<ProjectProjection> findByOrganizationId(UUID organizationId);
}