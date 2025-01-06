package com.martin.dayplanner.controller;

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

        // Initialiser kontrollere
        homeScreenController = new HomeScreenController(
                model.getHomeScreenModel(),
                view.getHomeScreenView(),
                this);

        // Sett initial visning
        updateActiveView();
    }

    public void updateActiveView() {
        // Finn den aktive modellen
        Object activeModel = model.getActiveModel();

        if (activeModel instanceof HomeScreen) {
            view.setCenterView(view.getHomeScreenView());
        } else if (activeModel instanceof Planner) {
            // Oppdater PlannerView med ny modell
            Planner activePlanner = (Planner) activeModel;
            view.updatePlannerView(activePlanner);

            // Opprett PlannerController kun når PlannerView er oppdatert
            if (plannerController == null || !plannerController.isManaging(activePlanner)) {
                plannerController = new PlannerController(
                        activePlanner,
                        view.getPlannerView(),
                        this);
            }
            view.setCenterView(view.getPlannerView());
        }
    }

}
