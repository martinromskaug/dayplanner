package com.martin.dayplanner.controller.planner;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.view.planner.PlannerView;
import com.martin.dayplanner.view.planner.popups.CreateTaskPopup;
import com.martin.dayplanner.view.planner.popups.EditTaskPopup;

import javafx.scene.control.ListView;

public class TaskActionHandler {
    private ControllablePlanner model;
    private PlannerView view;

    public TaskActionHandler(ControllablePlanner model, PlannerView view) {
        this.model = model;
        this.view = view;
    }

    public void handleAddTask() {
        CreateTaskPopup popup = new CreateTaskPopup();
        popup.setTaskCreationListener((name, dueDate, dueTime, priority) -> {
            if (model.addTask(name, dueDate, dueTime, priority)) {
                view.updateAllTaskLists();
            }
        });
        popup.showPopup();
    }

    public void handleRemoveTask() {
        for (ListView<String> taskList : view.getAllTaskLists()) {
            String selectedTaskName = taskList.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                model.removeTask(selectedTaskName);
                view.updateAllTaskLists();
            }
        }
    }

    public void handleEditTask() {
        for (ListView<String> taskList : view.getAllTaskLists()) {
            String selectedTaskName = taskList.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                Task taskToEdit = model.findTaskByName(selectedTaskName);
                if (taskToEdit != null) {
                    EditTaskPopup popup = new EditTaskPopup();
                    popup.setTaskEditListener((task, newName, newDueDate, newDueTime, newPriority) -> {
                        model.editTask(task, newName, newDueDate, newDueTime, newPriority);
                        view.updateAllTaskLists();
                    });
                    popup.showPopup(taskToEdit);
                }
            }
        }
    }

    public void setupListSelectionListener(ListView<String> listView) {
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean taskSelected = newSelection != null;
            view.getEditTaskButton().setVisible(taskSelected);
            view.getRemoveTaskButton().setVisible(taskSelected);
        });
    }
}
