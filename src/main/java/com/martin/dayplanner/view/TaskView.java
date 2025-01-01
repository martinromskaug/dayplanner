package com.martin.dayplanner.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.martin.dayplanner.controller.TaskController;

public class TaskView {

    private TaskController taskController;

    public TaskView(TaskController taskController) {
        this.taskController = taskController;
    }

    public void display(Stage stage) {
        VBox layout = new VBox(10);

        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter task name");

        Button addTaskButton = new Button("Add Task");
        ListView<String> taskListView = new ListView<>();
        Button removeTaskButton = new Button("Remove Selected Task");

        // Add task
        addTaskButton.setOnAction(e -> {
            String taskName = taskInput.getText();
            if (taskController.addTask(taskName)) {
                taskListView.getItems().add(taskName);
                taskInput.clear();
            }
        });

        // Remove task
        removeTaskButton.setOnAction(e -> {
            String selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null && taskController.removeTask(selectedTask)) {
                taskListView.getItems().remove(selectedTask);
            }
        });

        layout.getChildren().addAll(taskInput, addTaskButton, taskListView, removeTaskButton);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Day Planner");
        stage.show();
    }
}
