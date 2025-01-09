package com.martin.dayplanner.view.views.planner;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.views.BaseView;
import com.martin.dayplanner.view.Viewable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlannerView extends BaseView implements Viewable {

    private final ViewablePlanner model;
    private final Button addTaskButton;
    private final Button removeTaskButton;
    private final Button editTaskButton;
    private final Button goToMenuButton;
    private final ListView<String> newTasksListView;
    private final ListView<String> pendingTasksListView;
    private final ListView<String> completedTasksListView;
    private final BorderPane root;

    public PlannerView(ViewablePlanner model) {
        this.model = model;

        addTaskButton = createButton("Add Task");
        removeTaskButton = createButton("Remove Task");
        editTaskButton = createButton("Edit Task");
        goToMenuButton = createButton("Go To Menu");

        newTasksListView = createListView();
        pendingTasksListView = createListView();
        completedTasksListView = createListView();

        root = new BorderPane();
        updateTaskLists();
        setupLayout();
    }

    private void setupLayout() {
        root.setTop(createTopSection(model.getPlannerName()));
        root.setCenter(createCenterSection());
        root.setBottom(createButtonRow());
    }

    private GridPane createCenterSection() {
        GridPane taskGrid = new GridPane();
        taskGrid.setPadding(new Insets(10, 10, 10, 10));
        taskGrid.setHgap(5);
        taskGrid.setVgap(5);
        taskGrid.getStyleClass().add("center-grid");

        taskGrid.add(createSection("Not Started", newTasksListView, "plans-box"), 0, 0);
        taskGrid.add(createSection("Active Tasks", pendingTasksListView, "plans-box"), 1, 0);
        taskGrid.add(createSection("Completed Tasks", completedTasksListView, "plans-box"), 2, 0);

        return taskGrid;
    }

    private HBox createButtonRow() {
        HBox buttonRow = new HBox(10, goToMenuButton, removeTaskButton, editTaskButton, addTaskButton);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setPadding(new Insets(10));
        buttonRow.getStyleClass().add("button-row");
        return buttonRow;
    }

    public void updateTaskLists() {
        newTasksListView.getItems().setAll(getTaskNames(TaskStatus.NOTSTARTED));
        pendingTasksListView.getItems().setAll(getTaskNames(TaskStatus.ACTIVE));
        completedTasksListView.getItems().setAll(getTaskNames(TaskStatus.COMPLETED));
    }

    private List<String> getTaskNames(TaskStatus status) {
        return model.getTasksByStatus(status).stream()
                .sorted((task1, task2) -> task2.getPriority().compareTo(task1.getPriority()))
                .map(Task::getTaskName)
                .collect(Collectors.toList());
    }

    @Override
    public BorderPane getLayout() {
        return root;
    }

    public List<ListView<String>> getTaskLists() {
        List<ListView<String>> allTaskLists = new ArrayList<>();
        allTaskLists.add(newTasksListView);
        allTaskLists.add(pendingTasksListView);
        allTaskLists.add(completedTasksListView);
        return allTaskLists;
    }

    public String getPlannerName() {
        return model.getPlannerName();
    }

    public Button getEditTaskButton() {
        return editTaskButton;
    }

    public Button getRemoveTaskButton() {
        return removeTaskButton;
    }

    public Button getAddTaskButton() {
        return addTaskButton;
    }

    public Button getGoToMenuButton() {
        return goToMenuButton;
    }
}
