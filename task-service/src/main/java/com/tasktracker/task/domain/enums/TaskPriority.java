package com.tasktracker.task.domain.enums;

import java.time.Duration;

public enum TaskPriority {
    LOW(Duration.ofDays(30), "Низкий"),
    MEDIUM(Duration.ofDays(14), "Средний"),
    HIGH(Duration.ofDays(3), "Высокий"),
    CRITICAL(Duration.ofHours(24), "Критический");

    private final Duration sla;
    private final String displayName;

    TaskPriority(Duration sla, String displayName) {
        this.sla = sla;
        this.displayName = displayName;
    }

    public Duration getSla() {
        return sla;
    }

    public String getDisplayName() {
        return displayName;
    }
}
