package com.martin.dayplanner.controller;

import com.martin.dayplanner.view.AppView;

public class AppController {

    private final ControllableAppModel model;
    private final AppView view;

    public AppController(ControllableAppModel model, AppView view) {
        this.model = model;
        this.view = view;

        new HomeScreenController(model.getHomeScreenModel(), view.getHomeScreenView());
        new PlannerController(model.getPlannerModel(), view.getPlannerView());
    }
}
