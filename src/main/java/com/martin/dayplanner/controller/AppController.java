package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.view.AppView;
import com.martin.dayplanner.view.popups.CreateTaskPopup;
import com.martin.dayplanner.view.popups.EditTaskPopup;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class AppController {

    private ControllableDayPlanner model;
    private AppView view;

    public AppController(ControllableDayPlanner model, AppView view) {
        this.model = model;
        this.view = view;
        setupListeners();
    }

    private void setupListeners() {
        // Lytter for "Add Task"-knappen
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

        // Legg til lyttere for alle listene
        for (ListView<String> taskList : view.getAllTaskLists()) {
            setupListSelectionListener(taskList, view.getRemoveTaskButton(), view.getEditTaskButton());
        }

        // Logikk for "Remove Task"-knappen
        view.getRemoveTaskButton().setOnAction(e -> {
            for (ListView<String> taskList : view.getAllTaskLists()) {
                String selectedTaskName = taskList.getSelectionModel().getSelectedItem();
                if (selectedTaskName != null) {
                    model.removeTask(selectedTaskName);
                    view.updateAllTaskLists();
                }
            }
        });

        view.getEditTaskButton().setOnAction(e -> {
            for (ListView<String> taskList : view.getAllTaskLists()) {
                String selectedTaskName = taskList.getSelectionModel().getSelectedItem();
                if (selectedTaskName != null) {
                    Task taskToEdit = model.findTaskByName(selectedTaskName);
                    if (taskToEdit != null) {
                        EditTaskPopup popup = new EditTaskPopup();
                        popup.setTaskEditListener((task, newName, newDueDate, newDueTime, newPriority) -> {
                            task.setName(newName);
                            task.setDueDate(newDueDate);
                            task.setDueTime(newDueTime);
                            task.setPriority(newPriority);
                            view.updateAllTaskLists();
                        });
                        popup.showPopup(taskToEdit);
                        break;
                    }
                }
            }
        });

    }

    private void setupListSelectionListener(ListView<String> listView, Button removeButton, Button editButton) {
        // Kombinert lytter for både valg og fokus
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateButtonVisibility(listView, removeButton, editButton);
        });

        listView.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            updateButtonVisibility(listView, removeButton, editButton);
        });
    }

    // Metode for å oppdatere knappens synlighet
    private void updateButtonVisibility(ListView<String> listView, Button removeButton, Button editButton) {
        boolean taskSelected = listView.getSelectionModel().getSelectedItem() != null;
        editButton.setVisible(taskSelected);
        removeButton.setVisible(taskSelected);
    }

}
