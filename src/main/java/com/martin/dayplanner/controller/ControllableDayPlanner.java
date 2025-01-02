package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.Task;

public interface ControllableDayPlanner {

    public boolean addTask(Task task);

    public boolean removeTask(String taskToRemove);
}