package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.view.AppView;
import com.martin.dayplanner.view.popups.CreateTaskPopup;

public class AppController {

    private ControllableDayPlanner model;
    private AppView view;

    public AppController(ControllableDayPlanner model, AppView view) {
        this.model = model;
        this.view = view;
        setupListeners();
    }

    private void setupListeners() {
        view.getAddTaskButton().setOnAction(e -> {
            CreateTaskPopup popup = new CreateTaskPopup();
            popup.setTaskCreationListener((name, dueDate, dueTime, priority) -> {
                Task newTask = new Task(name);
                newTask.setDueDate(dueDate);
                newTask.setDueTime(dueTime);
                newTask.setPriority(priority);

                if (model.addTask(newTask)) {
                    view.updateAllTaskLists();
                }
            });
            popup.showPopup();
        });
    }

}
