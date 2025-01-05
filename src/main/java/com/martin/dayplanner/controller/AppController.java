package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.AppModel;
import com.martin.dayplanner.model.HomeScreen;
import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.view.AppView;

public class AppController {

    private final ControllableAppModel model;
    private final AppView view;
    private HomeScreenController homeScreenController;
    private PlannerController plannerController;

    public AppController(ControllableAppModel model, AppView view) {
        this.model = model;
        this.view = view;

        // Initialiser kontrollere for hver skjerm
        this.homeScreenController = new HomeScreenController(model.getHomeScreenModel(), view.getHomeScreenView(),
                this);
        this.plannerController = new PlannerController(model.getPlannerModel(), view.getPlannerView(), this);

        // Sett den første visningen (HomeScreen)
        updateActiveView();
    }

    public void updateActiveView() {
        // Finn den aktive modellen
        Object activeModel = model.getActiveModel();

        // Oppdater visningen basert på hvilken modell som er aktiv
        if (activeModel instanceof HomeScreen) {
            view.setCenterView(view.getHomeScreenView());
        } else if (activeModel instanceof Planner) {
            view.setCenterView(view.getPlannerView());
        }
    }
}
