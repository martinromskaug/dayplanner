package com.martin.dayplanner.model;

import java.util.ArrayList;
import java.util.List;

import com.martin.dayplanner.controller.ControllableDayPlanner;
import com.martin.dayplanner.view.ViewableDayPlanner;

public class Planner implements ControllableDayPlanner, ViewableDayPlanner {

    private List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public boolean addTask(Task task) {
        tasks.add(task);
        return true;
    }

    @Override
    public boolean removeTask(String taskName) {
        Task taskToRemove = tasks.stream()
                .filter(task -> task.getName().equals(taskName))
                .findFirst()
                .orElse(null);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);
            return true;
        }
        return false;
    }
}
