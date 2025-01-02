package com.martin.dayplanner.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class TaskView {

    private ViewableDayPlanner planner;
    private TextField taskInput;
    private Button addTaskButton;
    private Button removeTaskButton;
    private ListView<String> taskListView;

    public TaskView(ViewableDayPlanner planner) {
        this.planner = planner;

        taskInput = new TextField();
        taskInput.setPromptText("Enter task name");

        addTaskButton = new Button("Add Task");
        taskListView = new ListView<>();
        removeTaskButton = new Button("Remove Selected Task");

        updateTaskList();
    }

    public TextField getTaskInput() {
        return taskInput;
    }

    public Button getAddTaskButton() {
        return addTaskButton;
    }

    public ListView<String> getTaskListView() {
        return taskListView;
    }

    public Button getRemoveTaskButton() {
        return removeTaskButton;
    }

    public void updateTaskList() {
        taskListView.getItems().clear();
        taskListView.getItems().addAll(
                planner.getTasks().stream().map(task -> task.getName()).collect(Collectors.toList()));
    }

    public void display(Stage stage) {
        VBox layout = new VBox(10);
        layout.getChildren().addAll(taskInput, addTaskButton, taskListView, removeTaskButton);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Day Planner");
        stage.show();
    }
}
