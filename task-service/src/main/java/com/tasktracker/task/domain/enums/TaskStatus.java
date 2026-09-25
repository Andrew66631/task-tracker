package com.tasktracker.task.domain.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TaskStatus {
    NEW("Новая задача", 1),
    PROGRESS("в процессе", 2),
    REVIEW("ревью", 3),
    TEST("на тестe",4);

    private final String name;
    private final int number;
    private static final Map<Integer, TaskStatus> BY_NUMBER =
            Arrays.stream(values()).collect(Collectors.toMap(TaskStatus::getNumber, Function.identity()));

    TaskStatus(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getDisplayName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public static TaskStatus fromNumber(int number) {
        TaskStatus status = BY_NUMBER.get(number);
        if (status == null) {
            throw new IllegalArgumentException("Unknown TaskStatus number: " + number);
        }
        return status;
    }
}
