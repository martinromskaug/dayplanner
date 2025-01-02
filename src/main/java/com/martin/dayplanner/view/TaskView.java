package com.martin.dayplanner.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.martin.dayplanner.model.Task;
import com.martin.dayplanner.model.TaskStatus;

import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class TaskView {

    private ViewableDayPlanner planner;
    private TextField taskInput;
    private Button addTaskButton;
    private Button removeTaskButton;
    private Button completeTaskButton;
    private Button startTaskButton;

    private ListView<String> newTasksListView;
    private ListView<String> pendingTasksListView;
    private ListView<String> completedTasksListView;

    private Label dateLabel;
    private Label timeLabel;

    public TaskView(ViewableDayPlanner planner) {
        this.planner = planner;

        // Klokke og dato
        dateLabel = new Label();
        timeLabel = new Label();
        updateDateTime();

        // Oppdater dato og tid hvert sekund
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        taskInput = new TextField();
        taskInput.setPromptText("Enter task name");

        addTaskButton = new Button("Add Task");
        removeTaskButton = new Button("Remove Selected Task");
        completeTaskButton = new Button("Complete Selected Task");
        startTaskButton = new Button("Start Selected Task");

        newTasksListView = new ListView<>();
        pendingTasksListView = new ListView<>();
        completedTasksListView = new ListView<>();

        updateNewTasksList();
        updatePendingTasksList();
        updateCompletedTasksList();
    }

    // Getters for buttons
    public TextField getTaskInput() {
        return taskInput;
    }

    public Button getAddTaskButton() {
        return addTaskButton;
    }

    public Button getRemoveTaskButton() {
        return removeTaskButton;
    }

    public Button getCompleteTaskButton() {
        return completeTaskButton;
    }

    public Button getStartTaskButton() { // Ny metode
        return startTaskButton;
    }

    public ListView<String> getNewTasksListView() {
        return newTasksListView;
    }

    public ListView<String> getPendingTasksListView() {
        return pendingTasksListView;
    }

    public ListView<String> getCompletedTasksListView() {
        return completedTasksListView;
    }

    // Oppdateringsmetoder for listene
    public void updateNewTasksList() {
        newTasksListView.getItems().clear();
        newTasksListView.getItems().addAll(
                planner.getTasksByStatus(TaskStatus.NEW).stream()
                        .map(Task::getName)
                        .collect(Collectors.toList()));
    }

    public void updatePendingTasksList() {
        pendingTasksListView.getItems().clear();
        pendingTasksListView.getItems().addAll(
                planner.getTasksByStatus(TaskStatus.PENDING).stream()
                        .map(Task::getName)
                        .collect(Collectors.toList()));
    }

    public void updateCompletedTasksList() {
        completedTasksListView.getItems().clear();
        completedTasksListView.getItems().addAll(
                planner.getTasksByStatus(TaskStatus.COMPLETED).stream()
                        .map(Task::getName)
                        .collect(Collectors.toList()));
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText("Date: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeLabel.setText("Time: " + now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    // GUI-oppsett
    public void display(Stage stage) {
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                dateLabel, timeLabel, taskInput, addTaskButton, removeTaskButton, startTaskButton, completeTaskButton,
                new Label("New Tasks"), newTasksListView,
                new Label("Pending Tasks"), pendingTasksListView,
                new Label("Completed Tasks"), completedTasksListView);

        Scene scene = new Scene(layout, 400, 600);
        stage.setScene(scene);
        stage.setTitle("Day Planner");
        stage.show();
    }
}
