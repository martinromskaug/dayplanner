package com.martin.dayplanner.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;

import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class AppView {

    private ViewableDayPlanner planner;
    private TextField taskInput;

    private Button addTaskButton;
    private Button removeTaskButton;
    private Button editTaskButton;

    private ListView<String> newTasksListView;
    private ListView<String> pendingTasksListView;
    private ListView<String> completedTasksListView;

    private Label dateLabel;
    private Label timeLabel;

    public AppView(ViewableDayPlanner planner) {
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
        editTaskButton = new Button("Edit Selected Task");

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

    public Button getEditTaskButton() {
        return editTaskButton;
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

    public void display(Stage stage) {
        // Toppseksjon (tittel + dato/tid)
        Label titleLabel = new Label("Day Planner");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox topSection = new VBox(5, titleLabel, dateLabel, timeLabel);
        topSection.setStyle("-fx-alignment: center; -fx-padding: 10px;");

        // Oppgavelister
        Label newTasksLabel = new Label("New Tasks");
        Label pendingTasksLabel = new Label("Pending Tasks");
        Label completedTasksLabel = new Label("Completed Tasks");

        VBox newTasksColumn = new VBox(5, newTasksLabel, newTasksListView);
        VBox pendingTasksColumn = new VBox(5, pendingTasksLabel, pendingTasksListView);
        VBox completedTasksColumn = new VBox(5, completedTasksLabel, completedTasksListView);

        HBox taskColumns = new HBox(10, newTasksColumn, pendingTasksColumn, completedTasksColumn);
        taskColumns.setStyle("-fx-padding: 10px;");

        HBox buttonColumns = new HBox(5, removeTaskButton, editTaskButton, addTaskButton);
        buttonColumns.setStyle("-fx-alignment: center-right; -fx-padding: 10px;");

        // Hovedlayout
        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(taskColumns);
        root.setBottom(buttonColumns);

        // Scene og vindu
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Day Planner");
        stage.show();
    }

    public Stage getStage() {
        return null;
    }
}
