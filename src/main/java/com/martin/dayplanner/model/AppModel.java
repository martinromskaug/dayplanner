package com.martin.dayplanner.model;

import com.martin.dayplanner.controller.ControllableAppModel;
import com.martin.dayplanner.view.ViewableAppModel;

public class AppModel implements ViewableAppModel, ControllableAppModel {

    private final HomeScreen homeScreenModel;
    private Object activeModel;
    private String activePlanner;

    public AppModel() {
        this.homeScreenModel = new HomeScreen(this);
        this.activeModel = homeScreenModel;
        this.activePlanner = null;
    }

    @Override
    public Object getActiveModel() {
        return activeModel;
    }

    public void openPlanner(String plannerName) {
        // Hvis du vil støtte åpning av ulike planleggere basert på navn
        Planner selectedPlanner = homeScreenModel.findPlannerByName(plannerName);
        if (selectedPlanner != null) {
            activeModel = selectedPlanner;
            activePlanner = plannerName;
            System.out.println("Active model changed to Planner: " + plannerName);
        }
    }

    public void goToMenu() {
        this.activeModel = homeScreenModel;
        this.activePlanner = null;
    }

    @Override
    public HomeScreen getHomeScreenModel() {
        return homeScreenModel;
    }

    @Override
    public Planner getPlannerModel() {
        Planner plannerModel = homeScreenModel.findPlannerByName(activePlanner);
        return plannerModel;
    }
}
