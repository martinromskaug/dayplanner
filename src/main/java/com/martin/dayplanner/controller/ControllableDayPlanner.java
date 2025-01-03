package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.Task;

public interface ControllableDayPlanner {

    /**
     * Add a task to the planner
     * 
     * @param task the task to add
     * @return true if added
     */
    public boolean addTask(Task task);

    /**
     * Edit a task in the planner
     * 
     * @param taskName the name of the task to edit
     * @return true if edited
     */
    public boolean editTask(String taskName);

    /**
     * Remove a task from the planner
     * 
     * @param taskName the name of the task to move
     * @return true if removed
     */
    public boolean removeTask(String taskName);

    /**
     * Move a task to the next task status in the planner
     * 
     * @param taskName the task to move
     * @return true if moved
     */
    public boolean completeTaskStep(String taskName);

    public Task findTaskByName(String taskName);
}