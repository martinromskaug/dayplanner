package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;

public interface ControllableDayPlanner {

    public boolean addTask(Task task);

    public boolean removeTask(Task taskToRemove);

    public boolean updateTaskStatus(Task selectedTask, TaskStatus selectedStatus);

    public Task findTaskByName(String selectedTaskName);
}