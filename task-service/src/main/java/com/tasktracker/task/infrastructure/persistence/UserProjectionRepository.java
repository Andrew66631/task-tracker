package com.tasktracker.task.infrastructure.persistence;

import com.tasktracker.task.entity.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserProjectionRepository extends JpaRepository<UserProjection, UUID> {

    List<UserProjection> findByOrganizationId(UUID organizationId);
}