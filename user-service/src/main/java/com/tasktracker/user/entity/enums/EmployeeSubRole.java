package com.tasktracker.user.entity.enums;


public enum EmployeeSubRole {

    DEVELOPER("Разработчик"),
    TESTER("Тестировщик"),
    TEAM_LEAD("Тимлид"),
    PROJECT_MANAGER("Проектный менеджер");

    private final String displayName;

    EmployeeSubRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
