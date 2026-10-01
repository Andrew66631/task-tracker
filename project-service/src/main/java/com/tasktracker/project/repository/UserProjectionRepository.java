package com.tasktracker.project.repository;

import com.tasktracker.project.entity.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserProjectionRepository extends JpaRepository<UserProjection, UUID> {

    List<UserProjection> findByOrganizationId(UUID organizationId);

    List<UserProjection> findByRole(String role);
}