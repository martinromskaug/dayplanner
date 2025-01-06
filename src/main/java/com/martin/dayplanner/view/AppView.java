package com.martin.dayplanner.view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppView {

    private final BorderPane root;
    private final HomeScreenView homeScreenView;
    private PlannerView plannerView; // Kan være null til det settes

    public AppView(ViewableAppModel model) {
        this.root = new BorderPane();
        this.homeScreenView = new HomeScreenView(model.getHomeScreenModel());

        updatePlannerView(model.getPlannerModel());

        // Sett HomeScreenView som standardvisning
        setCenterView(homeScreenView);
    }

    public HomeScreenView getHomeScreenView() {
        return homeScreenView;
    }

    public PlannerView getPlannerView() {
        return plannerView;
    }

    public void updatePlannerView(ViewableDayPlanner plannerModel) {
        if (plannerModel != null) {
            this.plannerView = new PlannerView(plannerModel);
        } else {
            System.out.println("Planner model is null, cannot update PlannerView.");
        }
    }

    public void setCenterView(Viewable view) {
        root.setCenter(view.getLayout());
    }

    public void display(Stage stage) {
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Day Planner App");
        stage.show();
    }
}
