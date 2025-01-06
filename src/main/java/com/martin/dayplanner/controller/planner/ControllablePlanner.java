package com.martin.dayplanner.controller.planner;

import java.time.LocalDate;
import java.time.LocalTime;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskPriority;
import com.martin.dayplanner.model.task.TaskStatus;

/**
 * Interface for controlling a DayPlanner.
 * Provides methods for adding, removing, and updating tasks.
 */
public interface ControllablePlanner {

    /**
     * Add a task to the planner.
     * 
     * @param priority
     * @param dueTime
     * @param dueDate
     *
     * @param task     the task to add
     * @return true if the task was successfully added, false if a task with the
     *         same name already exists.
     */
    boolean addTask(String taskName, LocalDate dueDate, LocalTime dueTime, TaskPriority priority);

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

    void editTask(Task task, String newName, LocalDate newDueDate, LocalTime newDueTime, TaskPriority newPriority);

    void goToMenu();
}
