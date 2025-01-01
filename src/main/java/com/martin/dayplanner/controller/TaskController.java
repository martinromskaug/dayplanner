package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.Task;

public class TaskController {

    private ControllableDayPlanner planner;

    public TaskController(Planner planner) {
        this.planner = planner;
    }

    public boolean addTask(String taskName) {
        if (taskName == null || taskName.isEmpty()) {
            return false;
        }
        return planner.addTask(new Task(taskName));
    }

    public boolean removeTask(String taskName) {
        return planner.removeTask(taskName);
    }
}
