package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.PlannerView;

import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class TaskDragAndDropHandler {
    private ControllableDayPlanner model;
    private PlannerView view;

    public TaskDragAndDropHandler(ControllableDayPlanner model, PlannerView view) {
        this.model = model;
        this.view = view;
    }

    public void setupDragAndDropListeners() {
        for (ListView<String> sourceList : view.getAllTaskLists()) {
            for (ListView<String> targetList : view.getAllTaskLists()) {
                if (sourceList != targetList) {
                    setupDragAndDrop(sourceList, targetList);
                }
            }
        }
    }

    private void setupDragAndDrop(ListView<String> sourceList, ListView<String> targetList) {
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
                TaskStatus targetStatus = getStatusFromListView(targetList);
                if (targetStatus != null && model.updateTaskStatus(taskName, targetStatus)) {
                    view.updateAllTaskLists();
                }
                event.setDropCompleted(true);
            }
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
