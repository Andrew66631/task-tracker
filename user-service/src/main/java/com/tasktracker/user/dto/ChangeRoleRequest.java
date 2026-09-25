package com.tasktracker.user.dto;

import com.tasktracker.user.entity.enums.EmployeeSubRole;
import com.tasktracker.user.entity.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleRequest {

    @NotNull(message = "Role is required")
    private UserRole role;

    private EmployeeSubRole subRole;
}