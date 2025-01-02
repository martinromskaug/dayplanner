package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.Task;
import com.martin.dayplanner.view.TaskView;

public class TaskController {

    private ControllableDayPlanner planner;
    private TaskView view;

    public TaskController(Planner planner, TaskView view) {
        this.planner = planner;
        this.view = view;
        setupListeners();
    }

    private void setupListeners() {
        view.getAddTaskButton().setOnAction(e -> {
            String taskName = view.getTaskInput().getText();
            if (!taskName.isEmpty() && planner.addTask(new Task(taskName))) {
                view.getTaskInput().clear();
                view.updateTaskList();
            }
        });

        view.getRemoveTaskButton().setOnAction(e -> {
            String selectedTask = view.getTaskListView().getSelectionModel().getSelectedItem();
            if (selectedTask != null && planner.removeTask(selectedTask)) {
                view.updateTaskList();
            }
        });
    }

}
