package com.tasktracker.user.dto;

import com.tasktracker.user.entity.enums.EmployeeSubRole;
import com.tasktracker.user.entity.enums.UserRole;
import com.tasktracker.user.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
    private String roleName;
    private EmployeeSubRole subRole;
    private String subRoleName;
    private UserStatus status;
    private String statusName;
    private UUID organizationId;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
}