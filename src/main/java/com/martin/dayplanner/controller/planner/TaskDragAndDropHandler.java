package com.martin.dayplanner.controller.planner;

import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.views.ListItemData;
import com.martin.dayplanner.view.views.planner.PlannerView;

import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class TaskDragAndDropHandler {

    private final ControllablePlanner model;
    private final PlannerView view;

    public TaskDragAndDropHandler(ControllablePlanner model, PlannerView view) {
        this.model = model;
        this.view = view;
    }

    public void setupDragAndDropListeners() {
        for (ListView<ListItemData> sourceList : view.getTaskLists()) {
            for (ListView<ListItemData> targetList : view.getTaskLists()) {
                if (sourceList != targetList) {
                    setupDragAndDrop(sourceList, targetList);
                }
            }
        }
    }

    private void setupDragAndDrop(ListView<ListItemData> sourceList, ListView<ListItemData> targetList) {
        sourceList.setOnDragDetected(event -> {
            ListItemData selectedTask = sourceList.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                Dragboard dragboard = sourceList.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(selectedTask.getId()); // Legger oppgavens ID i dragboard
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
                String taskId = dragboard.getString();
                TaskStatus targetStatus = getStatusFromListView(targetList);
                if (targetStatus != null) {
                    model.updateTaskStatus(taskId, targetStatus);
                    view.updateTaskLists();
                }
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private TaskStatus getStatusFromListView(ListView<ListItemData> listView) {
        int index = view.getTaskLists().indexOf(listView);
        if (index == 0)
            return TaskStatus.NOTSTARTED;
        if (index == 1)
            return TaskStatus.ACTIVE;
        if (index == 2)
            return TaskStatus.COMPLETED;
        return null;
    }
}
