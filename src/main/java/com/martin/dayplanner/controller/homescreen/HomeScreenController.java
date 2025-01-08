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

        setupEventHandlers();
    }

    private void setupEventHandlers() {
        view.getCreateNewPlanButton().setOnAction(e -> createNewPlan());
        view.getGoToPlanButton().setOnAction(e -> goToSelectedPlan());
        view.getRemovePlanButton().setOnAction(e -> removeSelectedPlan());

        view.getPlansListView().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                goToSelectedPlan();
            }
        });
    }

    private void removeSelectedPlan() {
        String selectedPlanName = view.getPlansListView().getSelectionModel().getSelectedItem();

        if (selectedPlanName != null) {
            RemovePlanPopup popup = new RemovePlanPopup();
            boolean isConfirmed = popup.showPopup(selectedPlanName);

            if (isConfirmed) {
                model.removePlanner(selectedPlanName);
                view.updateHomeScreen();
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
                    view.updateHomeScreen();
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
        String selectedPlanName = view.getPlansListView().getSelectionModel().getSelectedItem();

        if (selectedPlanName != null) {
            model.openPlanner(selectedPlanName);
            appController.updateActiveView();
        } else {
            System.out.println("No plan selected.");
        }
    }

    public void updateHomeScreen() {
        view.updateHomeScreen();
    }

}
