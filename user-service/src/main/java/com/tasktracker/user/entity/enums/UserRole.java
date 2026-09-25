package com.tasktracker.user.entity.enums;

public enum UserRole {

    ADMIN("Администратор"),
    ORGANIZATION("Организация"),
    EMPLOYEE("Сотрудник"),
    USER("Пользователь");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
