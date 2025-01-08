package com.martin.dayplanner.controller.homescreen;

import com.martin.dayplanner.controller.AppController;
import com.martin.dayplanner.view.views.homescreen.HomeScreenView;
import com.martin.dayplanner.view.views.homescreen.popups.CreatePlanPopup;
import com.martin.dayplanner.view.views.homescreen.popups.RemovePlanPopup;

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
        // Koble knappene til deres respektive handlinger
        view.getCreateNewPlanButton().setOnAction(e -> createNewPlan());
        view.getGoToPlanButton().setOnAction(e -> goToSelectedPlan());
        view.getRemovePlanButton().setOnAction(e -> removeSelectedPlan());

        // Legg til dobbeltklikkshåndtering for å navigere til en plan
        view.getPlansListView().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Sjekk om det er dobbeltklikk
                goToSelectedPlan();
            }
        });
    }

    private void removeSelectedPlan() {
        String selectedPlanName = view.getPlansListView().getSelectionModel().getSelectedItem();

        if (selectedPlanName != null) {
            // Opprett og vis popup-en
            RemovePlanPopup popup = new RemovePlanPopup();
            boolean isConfirmed = popup.showPopup(selectedPlanName);

            // Slett planen hvis brukeren bekreftet
            if (isConfirmed) {
                model.removePlanner(selectedPlanName);
                view.updatePlannerList();
                System.out.println("Plan removed: " + selectedPlanName);
            } else {
                System.out.println("Plan removal canceled.");
            }
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

    public void updateHomeScreen() {
        view.updateHomeScreen();
    }

}
