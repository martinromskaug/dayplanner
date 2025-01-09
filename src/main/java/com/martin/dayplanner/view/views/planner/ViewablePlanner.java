package com.martin.dayplanner.view.views.planner;

import java.util.List;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;

public interface ViewablePlanner {

    public List<Task> getTasksByStatus(TaskStatus status);

    public String getPlannerName();
}