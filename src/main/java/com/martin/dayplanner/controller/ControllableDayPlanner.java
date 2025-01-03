package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;

public interface ControllableDayPlanner {

    /**
     * Add a task to the planner
     * 
     * @param task the task to add
     * @return true if added
     */
    public boolean addTask(Task task);

    /**
     * Remove a task from the planner
     * 
     * @param taskName the name of the task to move
     * @return true if removed
     */
    public boolean removeTask(String taskName);

    public Task findTaskByName(String taskName);

    public boolean updateTaskStatus(String taskName, TaskStatus targetStatus);
}