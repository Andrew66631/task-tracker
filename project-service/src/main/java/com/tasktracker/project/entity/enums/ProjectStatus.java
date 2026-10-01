package com.tasktracker.project.entity.enums;

public enum ProjectStatus {

    ACTIVE("Активный"),
    ARCHIVED("В архиве"),
    COMPLETED("Завершён");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}