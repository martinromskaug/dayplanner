package com.martin.dayplanner.model.task;

import com.martin.dayplanner.model.Planner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public class Task {

    private final String id;
    private String taskName;
    private transient Planner planner;
    private String plannerName;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalTime dueTime;

    public Task(String taskName, Planner planner) {
        this.taskName = taskName;
        this.planner = planner;
        this.plannerName = planner.getPlannerName();
        this.id = generateId(); // Generer unik ID
        this.status = TaskStatus.NOTSTARTED;
        this.priority = TaskPriority.LOW;
        this.dueDate = null;
        this.dueTime = null;
    }

    private String generateId() {
        return taskName + "-" + UUID.randomUUID(); // Kombiner navn og UUID
    }

    public String getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Planner getPlanner() {
        return planner;
    }

    public void setPlanner(Planner planner) {
        this.planner = planner;
    }

    public String getPlannerName() {
        return plannerName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id); // Sjekk basert på unik ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash basert på unik ID
    }
}
