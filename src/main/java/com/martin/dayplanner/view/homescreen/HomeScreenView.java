package com.martin.dayplanner.view.homescreen;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.view.Viewable;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class HomeScreenView implements Viewable {

    private final ViewableHomeScreen model;
    private final Button removePlanButton;
    private final Button createNewPlanButton;
    private final Button goToPlanButton;
    private final ListView<String> plansListView;
    private final ListView<String> deadlinesListView;
    private final ListView<String> activeTasksListView;
    private final TextField textBox;
    private final BorderPane root;

    public HomeScreenView(ViewableHomeScreen model) {
        this.model = model;

        // Initialiser knapper
        removePlanButton = new Button("Remove Plan");
        createNewPlanButton = new Button("Add Plan");
        goToPlanButton = new Button("Go to Plan");

        // Initialiser lister og tekstboks
        plansListView = new ListView<>();
        deadlinesListView = new ListView<>();
        activeTasksListView = new ListView<>();
        textBox = new TextField();
        textBox.setPromptText("New Planner Name");

        // Opprett root-layout
        root = new BorderPane();

        updatePlannerList();
        setupLayout();
    }

    private void setupLayout() {
        // Toppseksjon
        Label titleLabel = new Label("Day Planner");
        titleLabel.getStyleClass().add("title-label");

        // Dynamisk dato, år og klokke
        Label dateLabel = new Label();
        dateLabel.getStyleClass().add("date-label");

        Label yearLabel = new Label();
        yearLabel.getStyleClass().add("year-label");

        Label clockLabel = new Label();
        clockLabel.getStyleClass().add("clock-label");

        // Oppdater dato og klokke dynamisk
        updateDateTime(dateLabel, yearLabel, clockLabel);

        Timeline clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateDateTime(dateLabel, yearLabel, clockLabel);
        }));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();

        // Plasser dato og år vertikalt
        VBox dateYearBox = new VBox(5, dateLabel, yearLabel);
        dateYearBox.setAlignment(Pos.CENTER);

        // Kombiner dato, år og klokke horisontalt
        HBox dateTimeBox = new HBox(20, dateYearBox, clockLabel);
        dateTimeBox.setAlignment(Pos.CENTER_RIGHT);

        // Legg til toppseksjonen
        HBox titleBox = new HBox(titleLabel, dateTimeBox);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getStyleClass().add("title-box");
        titleBox.setPadding(new Insets(10, 20, 10, 20));

        root.setTop(titleBox);

        // Midtseksjon med GridPane
        GridPane centerGrid = new GridPane();
        centerGrid.getStyleClass().add("center-grid");
        centerGrid.setPadding(new Insets(20, 20, 30, 20));
        centerGrid.setHgap(15);
        centerGrid.setVgap(15);

        // Your Plans-seksjon
        Label plansLabel = new Label("Your Plans");
        plansLabel.getStyleClass().add("section-label");

        VBox plansBox = new VBox(10, plansListView, textBox, createButtonRow());
        plansBox.getStyleClass().add("plans-box");
        plansBox.setAlignment(Pos.TOP_CENTER);

        VBox plansContainer = new VBox(5, plansLabel, plansBox);
        plansContainer.setAlignment(Pos.TOP_CENTER);

        Label deadlinesLabel = new Label("Deadlines");
        deadlinesLabel.getStyleClass().add("section-label");

        VBox deadlinesBox = new VBox(deadlinesListView);
        deadlinesBox.getStyleClass().add("deadlines-box");
        deadlinesBox.setAlignment(Pos.TOP_CENTER);

        VBox deadlinesContainer = new VBox(5, deadlinesLabel, deadlinesBox);
        deadlinesContainer.setAlignment(Pos.TOP_CENTER);

        Label activeTasksLabel = new Label("Active Tasks");
        activeTasksLabel.getStyleClass().add("section-label");

        VBox activeTasksBox = new VBox(activeTasksListView);
        activeTasksBox.getStyleClass().add("tasks-box");
        activeTasksBox.setAlignment(Pos.TOP_CENTER);

        VBox activeTasksContainer = new VBox(5, activeTasksLabel, activeTasksBox);
        activeTasksContainer.setAlignment(Pos.TOP_CENTER);

        // Legg til elementer i GridPane
        centerGrid.add(plansContainer, 0, 0, 1, 2);
        centerGrid.add(deadlinesContainer, 1, 0);
        centerGrid.add(activeTasksContainer, 1, 1);

        root.setCenter(centerGrid);
    }

    private void updateDateTime(Label dateLabel, Label yearLabel, Label clockLabel) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd");
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        dateLabel.setText(now.format(dateFormatter));
        yearLabel.setText(now.format(yearFormatter));
        clockLabel.setText(now.format(timeFormatter));
    }

    private HBox createButtonRow() {
        HBox buttonRow = new HBox(10, removePlanButton, createNewPlanButton, goToPlanButton);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getStyleClass().add("button-row");
        return buttonRow;
    }

    public void updatePlannerList() {
        plansListView.getItems().setAll(
                model.getPlanners().stream()
                        .map(Planner::getPlannerName)
                        .collect(Collectors.toList()));
    }

    @Override
    public BorderPane getLayout() {
        return root;
    }

    public Button getRemovePlanButton() {
        return removePlanButton;
    }

    public Button getCreateNewPlanButton() {
        return createNewPlanButton;
    }

    public Button getGoToPlanButton() {
        return goToPlanButton;
    }

    public ListView<String> getPlansListView() {
        return plansListView;
    }

    public ListView<String> getDeadlinesListView() {
        return deadlinesListView;
    }

    public ListView<String> getActiveTasksListView() {
        return activeTasksListView;
    }

    public TextField getTextBox() {
        return textBox;
    }
}
