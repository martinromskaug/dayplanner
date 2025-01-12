package com.martin.dayplanner.model;

import com.martin.dayplanner.controller.planner.ControllablePlanner;
import com.martin.dayplanner.model.storage.StorageHandler;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskPriority;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.views.planner.ViewablePlanner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Planner implements ControllablePlanner, ViewablePlanner {

    private final String id;
    private String plannerName;
    private String groupId;
    private transient PlannerGroup plannerGroup;
    private transient AppModel appModel;
    private transient StorageHandler storageHandler;
    private LocalDate dueDate;
    private LocalTime dueTime;

    public Planner(String plannerName, PlannerGroup plannerGroup, AppModel appModel, StorageHandler storageHandler) {
        this.plannerName = plannerName;
        this.plannerGroup = plannerGroup;
        this.appModel = appModel;
        this.storageHandler = storageHandler;
        this.dueDate = null;
        this.dueTime = null;
        this.id = generateId();
        this.groupId = plannerGroup.getId();
    }

    private String generateId() {
        return plannerName + "-" + UUID.randomUUID();
    }

    public String getId() {
        return id;
    }

    @Override
    public String getPlannerName() {
        return plannerName;
    }

    public void setPlannerName(String newPlannerName) {
        this.plannerName = newPlannerName;
    }

    public PlannerGroup getPlannerGroup() {
        return plannerGroup;
    }

    public void setPlannerGroup(PlannerGroup plannerGroup) {
        this.plannerGroup = plannerGroup;
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
    public boolean addTask(String taskName, LocalDate dueDate, LocalTime dueTime, TaskPriority priority) {
        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }

        Task newTask = new Task(taskName, this);
        newTask.setDueDate(dueDate);
        newTask.setDueTime(dueTime);
        newTask.setPriority(priority);
        newTask.setStatus(TaskStatus.NOTSTARTED);

        try {
            storageHandler.addTaskToPlanner(plannerGroup, this, newTask);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeTask(String taskName) {
        try {
            Task taskToRemove = findTaskByName(taskName);

            if (taskToRemove != null) {
                storageHandler.removeTaskFromPlanner(plannerGroup, this, taskToRemove);
                return true;
            } else {
                System.err.println("Task not found: " + taskName);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error removing task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        if (storageHandler == null) {
            throw new IllegalStateException("StorageHandler is not initialized");
        }
        return storageHandler.getTasksForPlanner(plannerGroup, this)
                .stream()
                .filter(task -> task.getStatus() == status)
                .toList();
    }

    @Override
    public Task findTaskByName(String selectedTaskName) {
        return storageHandler.getTasksForPlanner(plannerGroup, this)
                .stream()
                .filter(task -> task.getTaskName().equals(selectedTaskName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateTaskStatus(String taskName, TaskStatus targetStatus) {
        Task taskToUpdate = findTaskByName(taskName);
        if (taskToUpdate != null && taskToUpdate.getStatus() != targetStatus) {
            taskToUpdate.setStatus(targetStatus);
            try {
                storageHandler.updateTask(plannerGroup, this, taskToUpdate);
                return true;
            } catch (Exception e) {
                System.err.println("Error updating task status: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    @Override
    public void editTask(Task task, String newName, LocalDate newDueDate, LocalTime newDueTime,
            TaskPriority newPriority) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }
        task.setTaskName(newName);
        task.setDueDate(newDueDate);
        task.setDueTime(newDueTime);
        task.setPriority(newPriority);

        try {
            storageHandler.updateTask(plannerGroup, this, task);
        } catch (Exception e) {
            System.err.println("Error editing task: " + e.getMessage());
        }
    }

    @Override
    public void goToMenu() {
        appModel.goToMenu();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Planner planner = (Planner) o;
        return Objects.equals(id, planner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setStorageHandler(StorageHandler storageHandler) {
        this.storageHandler = storageHandler;
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

}
