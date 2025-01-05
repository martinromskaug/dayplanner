package com.martin.dayplanner.model;

import com.martin.dayplanner.controller.ControllableDayPlanner;
import com.martin.dayplanner.model.storage.StorageHandler;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskPriority;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.ViewableDayPlanner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Planner implements ControllableDayPlanner, ViewableDayPlanner {

    private String plannerName;
    private final Map<TaskStatus, List<Task>> tasksByStatus;
    private final List<Task> allTasks;
    private final StorageHandler storageHandler;

    public Planner(String plannerName) {
        this.plannerName = plannerName;
        this.tasksByStatus = new HashMap<>();
        this.allTasks = new ArrayList<>();
        this.storageHandler = new StorageHandler();

        // Initialiser oppgaver fra lagring
        List<Task> loadedTasks = storageHandler.loadTasks();
        for (TaskStatus status : TaskStatus.values()) {
            tasksByStatus.put(status, new ArrayList<>());
        }
        for (Task task : loadedTasks) {
            allTasks.add(task);
            tasksByStatus.get(task.getStatus()).add(task);
        }
    }

    public String getPlannerName() {
        return plannerName;
    }

    public void setPlannerName(String newPlannerName) {
        this.plannerName = newPlannerName;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks);
    }

    @Override
    public boolean addTask(String taskName, LocalDate dueDate, LocalTime dueTime, TaskPriority priority) {
        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }
        Task newTask = new Task(taskName);
        newTask.setDueDate(dueDate);
        newTask.setDueTime(dueTime);
        newTask.setPriority(priority);

        if (!allTasks.contains(newTask)) {
            allTasks.add(newTask);
            tasksByStatus.get(newTask.getStatus()).add(newTask);
            saveTasks();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeTask(String taskName) {
        Task selectedTask = findTaskByName(taskName);
        if (selectedTask != null) {
            allTasks.remove(selectedTask);
            tasksByStatus.get(selectedTask.getStatus()).remove(selectedTask);
            saveTasks();
            return true;
        }
        return false;
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return new ArrayList<>(tasksByStatus.get(status));
    }

    @Override
    public Task findTaskByName(String selectedTaskName) {
        return allTasks.stream()
                .filter(task -> task.getName().equals(selectedTaskName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateTaskStatus(String taskName, TaskStatus targetStatus) {
        Task taskToUpdate = findTaskByName(taskName);
        if (taskToUpdate != null && taskToUpdate.getStatus() != targetStatus) {
            tasksByStatus.get(taskToUpdate.getStatus()).remove(taskToUpdate);
            taskToUpdate.setStatus(targetStatus);
            tasksByStatus.get(targetStatus).add(taskToUpdate);
            saveTasks();
            return true;
        }
        return false;
    }

    private void saveTasks() {
        storageHandler.saveTasks(allTasks);
    }

    @Override
    public void editTask(Task task, String newName, LocalDate newDueDate, LocalTime newDueTime,
            TaskPriority newPriority) {
        task.setName(newName);
        task.setDueDate(newDueDate);
        task.setDueTime(newDueTime);
        task.setPriority(newPriority);

        saveTasks();
    }
}
