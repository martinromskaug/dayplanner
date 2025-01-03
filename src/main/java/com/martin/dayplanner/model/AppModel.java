package com.martin.dayplanner.model;

import com.martin.dayplanner.controller.ControllableDayPlanner;
import com.martin.dayplanner.model.storage.StorageHandler;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.ViewableDayPlanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppModel implements ControllableDayPlanner, ViewableDayPlanner {

    private final Map<TaskStatus, List<Task>> tasksByStatus;
    private final List<Task> allTasks;
    private final StorageHandler storageHandler;

    public AppModel() {
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

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks);
    }

    @Override
    public boolean addTask(Task task) {
        if (!allTasks.contains(task)) {
            allTasks.add(task);
            tasksByStatus.get(task.getStatus()).add(task);
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
}
