package com.martin.dayplanner.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;

public class AppView {

    private final ViewableDayPlanner planner;

    private final Button addTaskButton;
    private final Button removeTaskButton;
    private final Button editTaskButton;

    private final ListView<String> newTasksListView;
    private final ListView<String> pendingTasksListView;
    private final ListView<String> completedTasksListView;

    private final Label dateLabel;
    private final Label timeLabel;

    public AppView(ViewableDayPlanner planner) {
        this.planner = planner;

        // Initialiser GUI-komponenter
        dateLabel = new Label();
        timeLabel = new Label();
        addTaskButton = new Button("Add Task");
        removeTaskButton = new Button("Remove Selected Task");
        editTaskButton = new Button("Edit Selected Task");
        newTasksListView = new ListView<>();
        pendingTasksListView = new ListView<>();
        completedTasksListView = new ListView<>();

        // Oppsett av komponenter
        setupDateTime();
        updateAllTaskLists();
    }

    private void setupDateTime() {
        updateDateTime();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText("Date: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeLabel.setText("Time: " + now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    public void updateAllTaskLists() {
        updateTaskList(newTasksListView, TaskStatus.NEW);
        updateTaskList(pendingTasksListView, TaskStatus.PENDING);
        updateTaskList(completedTasksListView, TaskStatus.COMPLETED);
    }

    private void updateTaskList(ListView<String> listView, TaskStatus status) {
        listView.getItems().setAll(
                planner.getTasksByStatus(status).stream()
                        .map(Task::getName)
                        .collect(Collectors.toList()));
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

    // GUI-oppsett
    public void display(Stage stage) {
        // Toppseksjon
        VBox topSection = new VBox(5, createTitleLabel(), dateLabel, timeLabel);
        topSection.setStyle("-fx-alignment: center; -fx-padding: 10px;");

        // Oppgavelister
        HBox taskColumns = new HBox(10,
                createTaskColumn("New Tasks", newTasksListView),
                createTaskColumn("Pending Tasks", pendingTasksListView),
                createTaskColumn("Completed Tasks", completedTasksListView));
        taskColumns.setStyle("-fx-padding: 10px;");

        // Knappeseksjon
        HBox buttonColumns = new HBox(5, removeTaskButton, editTaskButton, addTaskButton);
        buttonColumns.setStyle("-fx-alignment: center-right; -fx-padding: 10px;");

        // Hovedlayout
        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(taskColumns);
        root.setBottom(buttonColumns);

        // Scene og vindu
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Day Planner");
        stage.show();
    }

    private Label createTitleLabel() {
        Label titleLabel = new Label("Day Planner");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        return titleLabel;
    }

    private VBox createTaskColumn(String title, ListView<String> listView) {
        Label label = new Label(title);
        return new VBox(5, label, listView);
    }
}
