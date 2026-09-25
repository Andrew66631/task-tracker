package com.tasktracker.user.repository;

import com.tasktracker.user.entity.User;
import com.tasktracker.user.entity.enums.UserRole;
import com.tasktracker.user.entity.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findByStatus(UserStatus status);
    List<User> findByRole(UserRole role);
    List<User> findByStatusAndRole(UserStatus status, UserRole role);
    List<User> findByOrganizationId(UUID organizationId);
}