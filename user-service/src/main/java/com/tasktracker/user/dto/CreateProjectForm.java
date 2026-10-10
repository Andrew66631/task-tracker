package com.tasktracker.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateProjectForm {
    private String name;
    private String description;
    private UUID ownerId;
}