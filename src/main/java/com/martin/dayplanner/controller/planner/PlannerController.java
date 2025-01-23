package com.martin.dayplanner.controller.planner;

import com.martin.dayplanner.controller.AppController;
import com.martin.dayplanner.view.views.ListItemData;
import com.martin.dayplanner.view.views.planner.PlannerView;

import javafx.scene.control.ListView;

public class PlannerController {

    private ControllablePlanner model;
    private PlannerView view;
    private AppController appController;
    private TaskActionHandler actionHandler;
    private TaskDragAndDropHandler dragAndDropHandler;

    public PlannerController(ControllablePlanner model, PlannerView view, AppController appController) {
        this.model = model;
        this.view = view;
        this.appController = appController;
        this.actionHandler = new TaskActionHandler(model, view);
        this.dragAndDropHandler = new TaskDragAndDropHandler(model, view);
        setupListeners();
    }

    private void setupListeners() {

        view.getAddTaskButton().setOnAction(_ -> actionHandler.handleAddTask());

        for (ListView<ListItemData> taskList : view.getTaskLists()) {
            actionHandler.setupListSelectionListener(taskList);
        }
        view.getRemoveTaskButton().setOnAction(_ -> actionHandler.handleRemoveTask());
        view.getEditTaskButton().setOnAction(_ -> actionHandler.handleEditTask());

        dragAndDropHandler.setupDragAndDropListeners();

        view.getGoToMenuButton().setOnAction(_ -> {
            model.goToMenu();
            appController.updateActiveView();
        });
    }

    public boolean isManaging(ControllablePlanner planner) {
        return this.model.equals(planner);
    }

}
