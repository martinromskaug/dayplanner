package com.martin.dayplanner.controller.homescreen;

import com.martin.dayplanner.controller.AppController;
import com.martin.dayplanner.view.views.homescreen.HomeScreenView;
import com.martin.dayplanner.view.views.homescreen.popups.CreatePlanPopup;
import com.martin.dayplanner.view.views.homescreen.popups.EditPlanPopup;
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
        view.getRemovePlanButton().setOnAction(e -> removeSelectedPlan());
        view.getEditPlanButton().setOnAction(e -> editSelectedPlan());

        view.getPlansListView().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                goToSelectedPlan();
            }
        });
    }

    private void editSelectedPlan() {
        String selectedPlanName = view.getPlansListView().getSelectionModel().getSelectedItem();

        if (selectedPlanName != null) {
            EditPlanPopup popup = new EditPlanPopup(selectedPlanName);
            popup.setPlanEditListener(updatedPlanName -> {
                if (updatedPlanName != null && !updatedPlanName.isEmpty()) {
                    try {
                        model.editPlanner(selectedPlanName, updatedPlanName); // Oppdater modellens plan
                        view.updateHomeScreen(); // Oppdater visningen
                        System.out.println("Plan edited: " + selectedPlanName + " -> " + updatedPlanName);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Updated plan name cannot be empty.");
                }
            });
            popup.showPopup();
        } else {
            System.out.println("No plan selected.");
        }
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
