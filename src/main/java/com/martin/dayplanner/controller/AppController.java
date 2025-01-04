package com.martin.dayplanner.controller;

import com.martin.dayplanner.view.AppView;

import javafx.scene.control.ListView;

public class AppController {

    private AppView view;
    private TaskActionHandler actionHandler;
    private TaskDragAndDropHandler dragAndDropHandler;

    public AppController(ControllableDayPlanner model, AppView view) {
        this.view = view;
        this.actionHandler = new TaskActionHandler(model, view);
        this.dragAndDropHandler = new TaskDragAndDropHandler(model, view);
        setupListeners();
    }

    private void setupListeners() {

        view.getAddTaskButton().setOnAction(e -> actionHandler.handleAddTask());

        for (ListView<String> taskList : view.getAllTaskLists()) {
            actionHandler.setupListSelectionListener(taskList);
        }
        view.getRemoveTaskButton().setOnAction(e -> actionHandler.handleRemoveTask());
        view.getEditTaskButton().setOnAction(e -> actionHandler.handleEditTask());

        dragAndDropHandler.setupDragAndDropListeners();
    }
}
