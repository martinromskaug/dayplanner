package com.martin.dayplanner.view;

import com.martin.dayplanner.view.homescreen.HomeScreenView;
import com.martin.dayplanner.view.planner.PlannerView;
import com.martin.dayplanner.view.planner.ViewablePlanner;

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

        // Sett HomeScreenView som standardvisning
        setCenterView(homeScreenView);
    }

    public HomeScreenView getHomeScreenView() {
        return homeScreenView;
    }

    public PlannerView getPlannerView() {
        return plannerView;
    }

    public void updatePlannerView(ViewablePlanner plannerModel) {
        if (plannerModel != null) {
            if (plannerView == null || !plannerView.getPlannerName().equals(plannerModel.getPlannerName())) {
                this.plannerView = new PlannerView(plannerModel);
                System.out.println("PlannerView updated with new model: " + plannerModel.getPlannerName());
            }
        } else {
            this.plannerView = null; // Sett til null hvis modellen er null
            System.out.println("Planner model is null, PlannerView reset.");
        }
    }

    public void setCenterView(Viewable view) {
        if (view != null) {
            root.setCenter(view.getLayout());
        } else {
            System.out.println("View is null, cannot set center view.");
        }
    }

    public void display(Stage stage) {
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Day Planner App");
        stage.show();
    }
}
