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
import java.util.ArrayList;
import java.util.List;
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
        removeTaskButton.setVisible(false);
        editTaskButton = new Button("Edit Selected Task");
        editTaskButton.setVisible(false);
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

    public void updateAllTaskLists() {
        updateTaskList(newTasksListView, TaskStatus.NEW);
        updateTaskList(pendingTasksListView, TaskStatus.PENDING);
        updateTaskList(completedTasksListView, TaskStatus.COMPLETED);
    }

    public List<ListView<String>> getAllTaskLists() {
        List<ListView<String>> allTaskLists = new ArrayList<>();
        allTaskLists.add(newTasksListView);
        allTaskLists.add(pendingTasksListView);
        allTaskLists.add(completedTasksListView);
        return allTaskLists;
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

    public void display(Stage stage) {
        // Tittelseksjon (venstre halvdel)
        Label titleLabel = new Label("Day Planner");
        titleLabel.getStyleClass().add("title-label"); // Bruk CSS-klassen

        VBox titleBox = new VBox(titleLabel);
        titleBox.getStyleClass().add("title-box"); // Bruk CSS-klassen

        // Datoseksjon (høyre halvdel)
        dateLabel.getStyleClass().add("date-label"); // Bruk CSS-klassen
        timeLabel.getStyleClass().add("time-label"); // Bruk CSS-klassen

        VBox dateTimeBox = new VBox(5, dateLabel, timeLabel);
        dateTimeBox.getStyleClass().add("date-time-box"); // Bruk CSS-klassen

        // Kombiner toppseksjonen (venstre og høyre halvdel)
        HBox topSection = new HBox(titleBox, dateTimeBox);
        topSection.getStyleClass().add("top-section"); // Bruk CSS-klassen

        // Oppgavelister
        VBox allTasksColumn = createTaskColumn("All Tasks", newTasksListView);
        VBox todoColumn = createTaskColumn("To-Do", pendingTasksListView);
        VBox completedColumn = createTaskColumn("Completed Tasks", completedTasksListView);

        HBox taskColumns = new HBox(20, allTasksColumn, todoColumn, completedColumn); // Bruk CSS spacing
        taskColumns.getStyleClass().add("task-columns"); // Bruk CSS-klassen

        // Knappeseksjon
        addTaskButton.getStyleClass().add("button"); // Bruk CSS-knapp
        removeTaskButton.getStyleClass().add("button");
        editTaskButton.getStyleClass().add("button");

        HBox buttonColumns = new HBox(10, removeTaskButton, editTaskButton, addTaskButton); // Bruk CSS spacing
        buttonColumns.getStyleClass().add("button-columns"); // Bruk CSS-klassen

        // Hovedlayout
        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(taskColumns);
        root.setBottom(buttonColumns);

        // Legg til CSS
        Scene scene = new Scene(root, 800, 600); // Oppdatert vindustørrelse for bedre estetikk
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Sett opp scenen og vis den
        stage.setScene(scene);
        stage.setTitle("Day Planner");
        stage.show();
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        // Format: "Jan 31" for dato og "14:59" for klokkeslett
        String formattedDate = now.format(DateTimeFormatter.ofPattern("MMM dd"));
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));

        dateLabel.setText(formattedDate);
        timeLabel.setText(formattedTime);
    }

    private VBox createTaskColumn(String title, ListView<String> listView) {
        Label label = new Label(title);
        return new VBox(5, label, listView);
    }
}
