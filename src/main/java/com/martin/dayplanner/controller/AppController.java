package com.martin.dayplanner.controller;

import com.martin.dayplanner.controller.homescreen.HomeScreenController;
import com.martin.dayplanner.controller.planner.PlannerController;
import com.martin.dayplanner.model.HomeScreen;
import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.view.AppView;

public class AppController {

    private final ControllableAppModel model;
    private final AppView view;
    private HomeScreenController homeScreenController;
    private PlannerController plannerController; // Oppdateres kun ved behov

    public AppController(ControllableAppModel model, AppView view) {
        this.model = model;
        this.view = view;

        // Initialiser HomeScreenController én gang
        this.homeScreenController = new HomeScreenController(model.getHomeScreenModel(), view.getHomeScreenView(),
                this);

        // Sett initial visning
        updateActiveView();
    }

    public void updateActiveView() {
        Object activeModel = model.getActiveModel();

        if (activeModel instanceof HomeScreen) {
            view.setCenterView(view.getHomeScreenView());
            homeScreenController.updateHomeScreen();
        } else if (activeModel instanceof Planner) {
            Planner activePlanner = (Planner) activeModel;

            // Oppdater PlannerView
            view.updatePlannerView(activePlanner);

            // Opprett PlannerController hvis PlannerView ikke er null
            if (plannerController == null || !plannerController.isManaging(activePlanner)) {
                if (view.getPlannerView() != null) {
                    plannerController = new PlannerController(
                            activePlanner,
                            view.getPlannerView(),
                            this);
                } else {
                    System.out.println("PlannerView is null, cannot create PlannerController.");
                }
            }

            view.setCenterView(view.getPlannerView());
        } else {
            System.out.println("Unknown or null active model.");
        }
    }

}
