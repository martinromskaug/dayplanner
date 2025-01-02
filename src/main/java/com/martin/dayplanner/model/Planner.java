package com.martin.dayplanner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.martin.dayplanner.controller.ControllableDayPlanner;
import com.martin.dayplanner.view.ViewableDayPlanner;

public class Planner implements ControllableDayPlanner, ViewableDayPlanner {

    private List<Task> allTasks;

    public Planner() {
        this.allTasks = new ArrayList<>();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks);
    }

    @Override
    public boolean addTask(Task task) {
        allTasks.add(task);
        return true;
    }

    @Override
    public boolean removeTask(Task taskToRemove) {
        Task taskFound = allTasks.stream()
                .filter(task -> task.getName().equals(taskToRemove.getName()))
                .findFirst()
                .orElse(null);
        if (taskFound != null) {
            allTasks.remove(taskFound);
            return true;
        }
        return false;
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return allTasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateTaskStatus(Task selectedTask, TaskStatus selectedStatus) {
        Task task = allTasks.stream()
                .filter(t -> t.equals(selectedTask))
                .findFirst()
                .orElse(null);

        if (task != null) {
            task.setStatus(selectedStatus);
            return true;
        }
        return false;
    }

    @Override
    public Task findTaskByName(String selectedTaskName) {
        return allTasks.stream()
                .filter(task -> task.getName().equals(selectedTaskName))
                .findFirst()
                .orElse(null);
    }

}
