package com.martin.dayplanner.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;

public class PlannerView implements Viewable {

    private final ViewableDayPlanner planner;

    private final Button addTaskButton;
    private final Button removeTaskButton;
    private final Button editTaskButton;
    private final Button goToMenuButton;

    private final ListView<String> newTasksListView;
    private final ListView<String> pendingTasksListView;
    private final ListView<String> completedTasksListView;

    private final Label dateLabel;
    private final Label timeLabel;
    private final BorderPane root;

    public PlannerView(ViewableDayPlanner planner) {
        this.planner = planner;

        // Initialiser GUI-komponenter
        dateLabel = new Label();
        timeLabel = new Label();
        addTaskButton = new Button("Add Task");
        removeTaskButton = new Button("Remove Selected Task");
        removeTaskButton.setVisible(false);
        editTaskButton = new Button("Edit Selected Task");
        editTaskButton.setVisible(false);
        goToMenuButton = new Button("Go To Menu");
        newTasksListView = new ListView<>();
        pendingTasksListView = new ListView<>();
        completedTasksListView = new ListView<>();
        root = new BorderPane();

        // Oppsett av komponenter
        setupDateTime();
        updateAllTaskLists();
        setupLayout();
    }

    private void setupDateTime() {
        updateDateTime();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setupLayout() {
        // Tittelseksjon
        HBox topSection = createTopSection();

        // Oppgavelister
        HBox taskColumns = createTaskColumns();

        // Knappeseksjon
        HBox buttonColumns = createButtonSection();

        // Sett opp layout
        root.setTop(topSection);
        root.setCenter(taskColumns);
        root.setBottom(buttonColumns);
    }

    @Override
    public BorderPane getLayout() {
        return root;
    }

    private HBox createTopSection() {
        Label titleLabel = new Label("Day Planner");
        titleLabel.getStyleClass().add("title-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox dateTimeBox = new VBox(10, dateLabel, timeLabel);
        dateTimeBox.getStyleClass().add("date-time-box");

        return new HBox(titleLabel, spacer, dateTimeBox);
    }

    private HBox createTaskColumns() {
        VBox newTasks = createTaskColumn("New Tasks", newTasksListView);
        VBox pendingTasks = createTaskColumn("Pending Tasks", pendingTasksListView);
        VBox completedTasks = createTaskColumn("Completed Tasks", completedTasksListView);

        return new HBox(20, newTasks, pendingTasks, completedTasks);
    }

    private HBox createButtonSection() {
        return new HBox(10, goToMenuButton, removeTaskButton, editTaskButton, addTaskButton);
    }

    private VBox createTaskColumn(String title, ListView<String> listView) {
        Label label = new Label(title);
        return new VBox(5, label, listView);
    }

    public void updateAllTaskLists() {
        updateTaskList(newTasksListView, TaskStatus.NEW);
        updateTaskList(pendingTasksListView, TaskStatus.PENDING);
        updateTaskList(completedTasksListView, TaskStatus.COMPLETED);
    }

    private void updateTaskList(ListView<String> listView, TaskStatus status) {
        listView.getItems().setAll(
                planner.getTasksByStatus(status).stream()
                        .map(Task::getTaskName)
                        .collect(Collectors.toList()));
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();

        dateLabel.setText(now.format(DateTimeFormatter.ofPattern("MMM dd")));
        timeLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm")));
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

    public Button getGoToMenuButton() {
        return goToMenuButton;
    }

    public List<ListView<String>> getAllTaskLists() {
        List<ListView<String>> allTaskLists = new ArrayList<>();
        allTaskLists.add(newTasksListView);
        allTaskLists.add(pendingTasksListView);
        allTaskLists.add(completedTasksListView);
        return allTaskLists;
    }
}
