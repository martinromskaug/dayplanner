package com.martin.dayplanner.controller.planner;

import java.util.Map;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.views.PopupFieldKey;

public interface ControllablePlanner {

    void addTask(Map<PopupFieldKey, Object> taskData);

    void removeTask(String taskId);

    Task findTaskById(String taskId);

    void updateTaskStatus(String taskId, TaskStatus targetStatus);

    void editTask(Map<PopupFieldKey, Object> taskData);

    void goToMenu();
}
