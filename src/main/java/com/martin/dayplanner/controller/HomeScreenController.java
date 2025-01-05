package com.martin.dayplanner.controller;

import com.martin.dayplanner.view.HomeScreenView;

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
    }

    private void createNewPlan() {
        // Logikk for å opprette en ny plan
        model.addPlanner("New Plan"); // For testing, oppretter en plan med navn "New Plan"
        view.updatePlannerList(); // Oppdater visningen
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
