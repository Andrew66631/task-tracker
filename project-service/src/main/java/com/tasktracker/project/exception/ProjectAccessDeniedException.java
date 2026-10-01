package com.tasktracker.project.exception;

public class ProjectAccessDeniedException extends RuntimeException {

    public ProjectAccessDeniedException(String message) {
        super(message);
    }
}