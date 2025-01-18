package com.martin.dayplanner.view.views.planner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskPriority;
import com.martin.dayplanner.model.task.TaskStatus;

public interface ViewablePlanner {

    public List<Task> getTasksByStatus(TaskStatus status);

    public String getPlannerName();

    public String getTaskNameById(String taskId);

    public LocalDate getTaskDateById(String taskId);

    public LocalTime getTaskTimeById(String taskId);

    public TaskPriority getTaskPriorityById(String taskId);

    public String getId();
}