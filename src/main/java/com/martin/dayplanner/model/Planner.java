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

public class Planner implements ControllablePlanner, ViewablePlanner {

    private String plannerName;
    private AppModel appModel;
    private final StorageHandler storageHandler;

    public Planner(String plannerName, AppModel appModel, StorageHandler storageHandler) {
        this.appModel = appModel;
        this.plannerName = plannerName;
        this.storageHandler = storageHandler;
    }

    @Override
    public String getPlannerName() {
        return plannerName;
    }

    public void setPlannerName(String newPlannerName) {
        this.plannerName = newPlannerName;
    }

    @Override
    public List<Task> getAllTasks() {
        return storageHandler.getTasksForPlanner(plannerName);
    }

    @Override
    public boolean addTask(String taskName, LocalDate dueDate, LocalTime dueTime, TaskPriority priority) {
        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }

        Task newTask = new Task(taskName, plannerName);
        newTask.setDueDate(dueDate);
        newTask.setDueTime(dueTime);
        newTask.setPriority(priority);
        newTask.setStatus(TaskStatus.NOTSTARTED);

        try {
            storageHandler.addTaskToPlanner(plannerName, newTask);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeTask(String taskName) {
        try {
            storageHandler.removeTaskFromPlanner(plannerName, taskName);
            return true;
        } catch (Exception e) {
            System.err.println("Error removing task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return storageHandler.getTasksForPlanner(plannerName)
                .stream()
                .filter(task -> task.getStatus() == status)
                .toList();
    }

    @Override
    public Task findTaskByName(String selectedTaskName) {
        return storageHandler.getTasksForPlanner(plannerName)
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
                storageHandler.updateTask(taskToUpdate);
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
            storageHandler.updateTask(task);
        } catch (Exception e) {
            System.err.println("Error editing task: " + e.getMessage());
        }
    }

    @Override
    public void goToMenu() {
        appModel.goToMenu();
    }
}
