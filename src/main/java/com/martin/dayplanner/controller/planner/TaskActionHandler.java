package com.martin.dayplanner.controller.planner;

import com.martin.dayplanner.view.views.ListItemData;
import com.martin.dayplanner.view.views.PopupFieldKey;
import com.martin.dayplanner.view.views.PopupView;
import com.martin.dayplanner.view.views.planner.PlannerPopupConfigurator;
import com.martin.dayplanner.view.views.planner.PlannerView;

import javafx.scene.control.ListView;

public class TaskActionHandler {

    private final ControllablePlanner model;
    private final PlannerView view;
    private final PlannerPopupConfigurator popupConfigurator;

    public TaskActionHandler(ControllablePlanner model, PlannerView view) {
        this.model = model;
        this.view = view;
        this.popupConfigurator = view.getPopup();
    }

    public void handleAddTask() {
        PopupView popup = popupConfigurator.createTaskPopup();

        popup.setActions(taskData -> {
            model.addTask(taskData);
            view.updateTaskLists();
            System.out.printf("Task created: %s%n", taskData.get(PopupFieldKey.NAME));
        }, null);

        popup.show();
    }

    public void handleRemoveTask() {
        for (ListView<ListItemData> taskList : view.getTaskLists()) {
            ListItemData selectedItem = taskList.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                String taskId = selectedItem.getId();
                model.removeTask(taskId);
                view.updateTaskLists();
                System.out.printf("Task removed: ID %s%n", taskId);
                break;
            }
        }
    }

    public void handleEditTask() {
        for (ListView<ListItemData> taskList : view.getTaskLists()) {
            ListItemData selectedItem = taskList.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                String taskId = selectedItem.getId();
                PopupView popup = popupConfigurator.editTaskPopup(taskId);

                popup.setActions(taskData -> {
                    model.editTask(taskData);
                    view.updateTaskLists();
                    System.out.printf("Task updated: ID %s -> %s%n", taskId, taskData.get(PopupFieldKey.NAME));
                }, null);

                popup.show();
                break;
            }
        }
    }

    public void setupListSelectionListener(ListView<ListItemData> listView) {
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean taskSelected = newSelection != null;
            view.getEditTaskButton().setVisible(taskSelected);
            view.getRemoveTaskButton().setVisible(taskSelected);
        });
    }
}
