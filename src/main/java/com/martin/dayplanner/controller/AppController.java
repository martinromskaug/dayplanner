package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.AppView;
import com.martin.dayplanner.view.popups.CreateTaskPopup;
import com.martin.dayplanner.view.popups.EditTaskPopup;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

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

        // Legg til lyttere for drag-and-drop mellom listene
        for (ListView<String> sourceList : view.getAllTaskLists()) {
            for (ListView<String> targetList : view.getAllTaskLists()) {
                if (sourceList != targetList) {
                    setupDragAndDrop(sourceList, targetList);
                }
            }
        }

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

    private void setupDragAndDrop(ListView<String> sourceList, ListView<String> targetList) {
        // Når en oppgave dras fra sourceList
        sourceList.setOnDragDetected(event -> {
            String selectedTaskName = sourceList.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                Dragboard dragboard = sourceList.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(selectedTaskName);
                dragboard.setContent(content);
                event.consume();
            }
        });

        // Når oppgaven slippes i targetList
        targetList.setOnDragOver(event -> {
            if (event.getGestureSource() != targetList && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        targetList.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString()) {
                String taskName = dragboard.getString();

                // Finn statusen til targetList ved hjelp av modellen
                TaskStatus targetStatus = getStatusFromListView(targetList);

                if (targetStatus != null && model.updateTaskStatus(taskName, targetStatus)) {
                    view.updateAllTaskLists();
                }
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

        // Når drag operasjonen er ferdig
        sourceList.setOnDragDone(event -> {
            event.consume();
        });
    }

    private TaskStatus getStatusFromListView(ListView<String> listView) {
        int index = view.getAllTaskLists().indexOf(listView);
        if (index == 0)
            return TaskStatus.NEW;
        if (index == 1)
            return TaskStatus.PENDING;
        if (index == 2)
            return TaskStatus.COMPLETED;
        return null;
    }

}
