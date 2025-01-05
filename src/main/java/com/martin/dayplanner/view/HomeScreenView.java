package com.martin.dayplanner.view;

import java.util.stream.Collectors;
import com.martin.dayplanner.model.Planner;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HomeScreenView {

    private final ViewableHomeScreen model;
    private final Button createNewPlanButton;
    private final Button goToPlanButton;
    private final ListView<String> plansListView;
    private final BorderPane root;

    public HomeScreenView(ViewableHomeScreen model) {
        this.model = model;
        createNewPlanButton = new Button("Create New Plan");
        goToPlanButton = new Button("Go to Plan");
        plansListView = new ListView<>();
        root = new BorderPane();

        // Oppdater liste med planer
        updatePlannerList();

        // Opprett layout
        setupLayout();
    }

    private void setupLayout() {
        // Tittel
        Label titleLabel = new Label("Day Planner");
        titleLabel.getStyleClass().add("title-label");

        // Planer-seksjon
        VBox plansBox = new VBox(10, new Label("Your Plans"), plansListView);
        plansBox.getStyleClass().add("plans-box");

        // Knapp-seksjon
        VBox buttonBox = new VBox(10, createNewPlanButton, plansBox, goToPlanButton);
        buttonBox.setMaxWidth(200);
        buttonBox.setStyle("-fx-alignment: center;");

        // Sett layout i BorderPane
        root.setTop(createCenteredTitle(titleLabel));
        root.setCenter(buttonBox);
    }

    private HBox createCenteredTitle(Label title) {
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        return new HBox(leftSpacer, title, rightSpacer);
    }

    public void updatePlannerList() {
        plansListView.getItems().setAll(
                model.getPlanners().stream()
                        .map(Planner::getPlannerName)
                        .collect(Collectors.toList()));
    }

    // Eksponerer root layout
    public BorderPane getLayout() {
        return root;
    }

    // Gettere for knappene og ListView
    public Button getCreateNewPlanButton() {
        return createNewPlanButton;
    }

    public Button getGoToPlanButton() {
        return goToPlanButton;
    }

    public ListView<String> getPlansListView() {
        return plansListView;
    }
}
