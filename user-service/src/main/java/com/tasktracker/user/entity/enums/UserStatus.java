package com.tasktracker.user.entity.enums;

public enum UserStatus {

    PENDING("Ожидает одобрения"),
    ACTIVE("Активен"),
    BLOCKED("Заблокирован"),
    REJECTED("Отклонён");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
