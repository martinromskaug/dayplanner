package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.TaskView;

public class TaskController {

    private ControllableDayPlanner planner;
    private TaskView view;

    public TaskController(ControllableDayPlanner planner, TaskView view) {
        this.planner = planner;
        this.view = view;
        setupListeners();
    }

    private void setupListeners() {
        view.getAddTaskButton().setOnAction(e -> {
            String taskName = view.getTaskInput().getText();
            if (!taskName.isEmpty() && planner.addTask(new Task(taskName))) {
                view.getTaskInput().clear();
                view.updateNewTasksList();
            }
        });

        view.getStartTaskButton().setOnAction(e -> {
            String selectedTaskName = view.getNewTasksListView().getSelectionModel().getSelectedItem();
            Task taskToStart = planner.findTaskByName(selectedTaskName);
            if (taskToStart != null && planner.updateTaskStatus(taskToStart, TaskStatus.PENDING)) {
                view.updateNewTasksList();
                view.updatePendingTasksList();
            }
        });

        view.getCompleteTaskButton().setOnAction(e -> {
            String selectedTaskName = view.getPendingTasksListView().getSelectionModel().getSelectedItem();
            Task taskToComplete = planner.findTaskByName(selectedTaskName);
            if (taskToComplete != null && planner.updateTaskStatus(taskToComplete, TaskStatus.COMPLETED)) {
                view.updatePendingTasksList();
                view.updateCompletedTasksList();
            }
        });

        view.getRemoveTaskButton().setOnAction(e -> {
            String selectedTaskName = view.getNewTasksListView().getSelectionModel().getSelectedItem();
            Task taskToRemove = planner.findTaskByName(selectedTaskName);
            if (taskToRemove != null && planner.removeTask(taskToRemove)) {
                view.updateNewTasksList();
            }
        });
    }
}
