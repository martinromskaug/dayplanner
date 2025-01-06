package com.martin.dayplanner.view;

import java.util.List;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;

public interface ViewableDayPlanner {

    public List<Task> getAllTasks();

    public List<Task> getTasksByStatus(TaskStatus status);

    public String getPlannerName();
}