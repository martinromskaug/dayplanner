package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;

/**
 * Interface for controlling a DayPlanner.
 * Provides methods for adding, removing, and updating tasks.
 */
public interface ControllableDayPlanner {

    /**
     * Add a task to the planner.
     *
     * @param task the task to add
     * @return true if the task was successfully added, false if a task with the
     *         same name already exists.
     */
    boolean addTask(Task task);

    /**
     * Remove a task from the planner.
     *
     * @param taskName the name of the task to remove
     * @return true if the task was successfully removed, false if the task was
     *         not found.
     */
    boolean removeTask(String taskName);

    /**
     * Find a task by its name.
     *
     * @param taskName the name of the task to find
     * @return the task if it was found, null otherwise.
     */
    Task findTaskByName(String taskName);

    /**
     * Update the status of a task.
     *
     * @param taskName     the name of the task to update
     * @param targetStatus the new status to set
     * @return true if the status was successfully updated, false if the task was
     *         not found or the status update failed.
     */
    boolean updateTaskStatus(String taskName, TaskStatus targetStatus);
}
