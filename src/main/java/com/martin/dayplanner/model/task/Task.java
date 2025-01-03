package com.martin.dayplanner.model.task;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {

    private String name;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalTime dueTime;

    public Task(String name) {
        this.name = name;
        this.status = TaskStatus.NEW;
        this.priority = null;
        this.dueDate = null;
        this.dueTime = null;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalTime dueTime) {
        this.dueTime = dueTime;
    }

}
