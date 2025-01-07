package com.martin.dayplanner.controller.homescreen;

import com.martin.dayplanner.controller.AppController;
import com.martin.dayplanner.view.views.homescreen.HomeScreenView;
import com.martin.dayplanner.view.views.homescreen.popups.CreatePlanPopup;

public class HomeScreenController {

    private ControllableHomeScreen model;
    private HomeScreenView view;
    private AppController appController;

    public HomeScreenController(ControllableHomeScreen model, HomeScreenView view, AppController appController) {
        this.model = model;
        this.view = view;
        this.appController = appController;

        // Koble knappene til deres respektive handlinger
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        // Opprett ny plan når "Create New Plan"-knappen trykkes
        view.getCreateNewPlanButton().setOnAction(e -> createNewPlan());

        // Naviger til valgt plan når "Go to Plan"-knappen trykkes
        view.getGoToPlanButton().setOnAction(e -> goToSelectedPlan());

        view.getRemovePlanButton().setOnAction(e -> removeSelectedPlan());
    }

    private void removeSelectedPlan() {
        String selectedPlanName = view.getPlansListView().getSelectionModel().getSelectedItem();

        if (selectedPlanName != null) {
            // Naviger til valgt plan
            model.removePlanner(selectedPlanName);
            view.updatePlannerList();
        } else {
            System.out.println("No plan selected.");
        }
    }

    private void createNewPlan() {
        CreatePlanPopup popup = new CreatePlanPopup();
        popup.setPlanCreationListener(planName -> {
            if (planName != null && !planName.isEmpty()) {
                try {
                    model.addPlanner(planName);
                    view.updatePlannerList(); // Oppdater listen med planer
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            } else {
                System.err.println("Plan name cannot be empty.");
            }
        });
        popup.showPopup();
    }

    private void goToSelectedPlan() {
        // Hent valgt plan fra visningen
        String selectedPlanName = view.getPlansListView().getSelectionModel().getSelectedItem();

        if (selectedPlanName != null) {
            // Naviger til valgt plan
            model.openPlanner(selectedPlanName); // Åpner den valgte planen
            appController.updateActiveView();
        } else {
            System.out.println("No plan selected.");
        }
    }

}
