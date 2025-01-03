package com.martin.dayplanner.controller;

import java.time.LocalDate;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskPriority;
import com.martin.dayplanner.model.task.TaskStatus;
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

    }
}
