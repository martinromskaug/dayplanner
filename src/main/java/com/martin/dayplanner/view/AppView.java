package com.martin.dayplanner.view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppView {

    private final BorderPane root;
    private final HomeScreenView homeScreenView;
    private final PlannerView plannerView;

    public AppView(ViewableAppModel model) {
        this.root = new BorderPane();
        this.homeScreenView = new HomeScreenView(model.getHomeScreenModel());
        this.plannerView = new PlannerView(model.getPlannerModel());

        // Sett HomeScreenView som standardvisning
        setCenterHomeScreen();
    }

    public HomeScreenView getHomeScreenView() {
        return homeScreenView;
    }

    public PlannerView getPlannerView() {
        return plannerView;
    }

    public void setCenterHomeScreen() {
        root.setCenter(homeScreenView.getLayout());
    }

    public void setCenterPlanner() {
        root.setCenter(plannerView.getLayout());
    }

    public void display(Stage stage) {
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Day Planner App");
        stage.show();
    }
}
