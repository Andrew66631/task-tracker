package com.tasktracker.task.exception;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(UUID id) {
        super("Задача с таким  id: " + id + " отсутствует");
    }
}