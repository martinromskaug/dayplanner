package com.martin.dayplanner.model;

import com.martin.dayplanner.controller.ControllableAppModel;
import com.martin.dayplanner.view.ViewableAppModel;

public class AppModel implements ViewableAppModel, ControllableAppModel {

    private final HomeScreen homeScreenModel;
    private final Planner plannerModel;

    public AppModel() {
        this.homeScreenModel = new HomeScreen();
        this.plannerModel = new Planner("My first plan");
    }

    @Override
    public HomeScreen getHomeScreenModel() {
        return homeScreenModel;
    }

    @Override
    public Planner getPlannerModel() {
        return plannerModel;
    }
}
