package com.martin.dayplanner.view.planner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.Viewable;

public class PlannerView implements Viewable {

    private final ViewablePlanner planner;

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

    public PlannerView(ViewablePlanner planner) {
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
        // Toppseksjon
        HBox topSection = createTopSection();

        // Oppgavelister
        GridPane taskColumns = createTaskColumns();

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
        // Tittel
        Label titleLabel = new Label(planner.getPlannerName());
        titleLabel.getStyleClass().add("title-label");

        // Dato og klokke
        Label dateLabel = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd")));
        dateLabel.getStyleClass().add("date-label");

        Label yearLabel = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")));
        yearLabel.getStyleClass().add("year-label");

        VBox dateBox = new VBox(2, dateLabel, yearLabel);
        dateBox.setAlignment(Pos.CENTER_LEFT);

        Label timeLabel = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.getStyleClass().add("clock-label");

        HBox dateTimeBox = new HBox(10, dateBox, timeLabel);
        dateTimeBox.setAlignment(Pos.CENTER_RIGHT);
        dateTimeBox.getStyleClass().add("date-time-box");

        // Toppseksjon med tittel og dato/klokke
        HBox topBox = new HBox(titleLabel, dateTimeBox);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 20, 20, 20));
        HBox.setHgrow(dateTimeBox, Priority.ALWAYS);

        return topBox;
    }

    private GridPane createTaskColumns() {
        GridPane taskGrid = new GridPane();
        taskGrid.setPadding(new Insets(20, 20, 20, 20));
        taskGrid.setHgap(5);
        taskGrid.setVgap(20);
        taskGrid.getStyleClass().add("task-grid");

        VBox newTasks = createTaskColumn("New Tasks", newTasksListView);
        VBox pendingTasks = createTaskColumn("Pending Tasks", pendingTasksListView);
        VBox completedTasks = createTaskColumn("Completed Tasks", completedTasksListView);

        taskGrid.add(newTasks, 0, 0);
        taskGrid.add(pendingTasks, 1, 0);
        taskGrid.add(completedTasks, 2, 0);

        return taskGrid;
    }

    private VBox createTaskColumn(String title, ListView<String> listView) {
        Label columnLabel = new Label(title);
        columnLabel.getStyleClass().add("section-label");

        VBox listBox = new VBox(listView);
        listBox.getStyleClass().add("plans-box"); // Legg til samme stil som i HomeScreenView

        VBox columnBox = new VBox(10, columnLabel, listBox);
        columnBox.setAlignment(Pos.TOP_CENTER);
        columnBox.getStyleClass().add("task-column");
        columnBox.setPadding(new Insets(10));
        return columnBox;
    }

    private HBox createButtonSection() {
        HBox buttonRow = new HBox(10, goToMenuButton, removeTaskButton, editTaskButton, addTaskButton);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setPadding(new Insets(10, 20, 20, 20));
        buttonRow.getStyleClass().add("button-row");

        return buttonRow;
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

    public String getPlannerName() {
        return planner.getPlannerName();
    }
}
