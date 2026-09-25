package com.tasktracker.task.domain.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TaskType {

    DEVELOPMENT("Разработка", 1),
    TESTING("Тестирование", 2),
    DESIGN("Дизайн", 3),
    ANALYTICS("Аналитика", 4),
    DEVOPS("DevOps", 5),
    DOCUMENTATION("Документация", 6);

    private final String name;
    private final int number;

    private static final Map<Integer, TaskType> BY_NUMBER =
            Collections.unmodifiableMap(
                    Arrays.stream(values())
                            .collect(Collectors.toMap(TaskType::getNumber, Function.identity()))
            );

    TaskType(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getDisplayName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public static TaskType fromNumber(int number) {
        TaskType type = BY_NUMBER.get(number);
        if (type == null) {
            throw new IllegalArgumentException("Unknown TaskType number: " + number);
        }
        return type;
    }
}