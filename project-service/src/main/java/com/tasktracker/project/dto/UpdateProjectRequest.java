package com.tasktracker.project.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {

    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;
}