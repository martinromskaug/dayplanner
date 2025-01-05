package com.martin.dayplanner.model;

import com.martin.dayplanner.controller.ControllableAppModel;
import com.martin.dayplanner.view.ViewableAppModel;

public class AppModel implements ViewableAppModel, ControllableAppModel {

    private final HomeScreen homeScreenModel;
    private final Planner plannerModel;
    private Object activeModel;

    public AppModel() {
        this.homeScreenModel = new HomeScreen(this);
        this.plannerModel = new Planner("My first plan", this);
        this.activeModel = plannerModel;
    }

    @Override
    public HomeScreen getHomeScreenModel() {
        return homeScreenModel;
    }

    @Override
    public Planner getPlannerModel() {
        return plannerModel;
    }

    public void goToMenu() {
        activeModel = homeScreenModel;
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
            System.out.println("Active model changed to Planner: " + plannerName);
        }
    }
}
