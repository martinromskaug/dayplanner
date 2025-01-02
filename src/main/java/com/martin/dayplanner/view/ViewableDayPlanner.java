package com.martin.dayplanner.view;

import java.util.List;

import com.martin.dayplanner.model.Task;
import com.martin.dayplanner.model.TaskStatus;

public interface ViewableDayPlanner {

    public List<Task> getAllTasks();

    public List<Task> getTasksByStatus(TaskStatus status);
}