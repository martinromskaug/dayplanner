package com.martin.dayplanner.controller.homescreen;

import com.martin.dayplanner.controller.AppController;
import com.martin.dayplanner.view.views.homescreen.GroupTreeItem;
import com.martin.dayplanner.view.views.homescreen.HomeScreenView;
import com.martin.dayplanner.view.views.homescreen.popups.CreatePlanPopup;
import com.martin.dayplanner.view.views.homescreen.popups.EditPlanPopup;
import com.martin.dayplanner.view.views.homescreen.popups.RemovePlanPopup;
import javafx.scene.control.TreeItem;

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
        view.getPlansTreeView().getRoot().getChildren().forEach(item -> {
            if (item instanceof GroupTreeItem) {
                GroupTreeItem groupItem = (GroupTreeItem) item; // Manuell casting
                groupItem.getAddButton().setOnAction(e -> handleAddPlanner(groupItem.getGroupName()));
            }
        });

        view.getCreateNewPlanButton().setOnAction(e -> createNewPlan());
        view.getRemovePlanButton().setOnAction(e -> removeSelectedPlan());
        view.getEditPlanButton().setOnAction(e -> editSelectedPlan());
        view.getAddGroupButton().setOnAction(e -> createNewGroup());

        view.getPlansTreeView().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                goToSelectedPlan();
            }
        });
    }

    private void editSelectedPlan() {
        TreeItem<String> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.isLeaf()) {
            String selectedPlanName = selectedItem.getValue();
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
            System.out.println("No plan selected or selection is not a plan.");
        }
    }

    private void removeSelectedPlan() {
        TreeItem<String> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.isLeaf()) {
            String selectedPlanName = selectedItem.getValue();
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
            System.out.println("No plan selected or selection is not a plan.");
        }
    }

    private void createNewPlan() {
        TreeItem<String> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            String selectedGroupName = selectedItem.getValue(); // Henter gruppenavnet fra valgt rot
            CreatePlanPopup popup = new CreatePlanPopup();
            popup.setPlanCreationListener(planName -> {
                if (planName != null && !planName.isEmpty()) {
                    try {
                        model.addPlannerToGroup(planName, selectedGroupName); // Legger planen til den valgte gruppen
                        view.updateHomeScreen(); // Oppdaterer visningen
                        System.out.println("Plan created: " + planName + " in group: " + selectedGroupName);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Plan name cannot be empty.");
                }
            });
            popup.showPopup();
        } else {
            System.out.println("No group selected or selection is not a group.");
        }
    }

    private void createNewGroup() {
        CreatePlanPopup popup = new CreatePlanPopup();
        popup.setPlanCreationListener(planName -> {
            if (planName != null && !planName.isEmpty()) {
                try {
                    model.addPlannerGroup(planName);
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
        TreeItem<String> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.isLeaf()) {
            String selectedPlanName = selectedItem.getValue();
            model.openPlanner(selectedPlanName);
            appController.updateActiveView();
        } else {
            System.out.println("No plan selected or selection is not a plan.");
        }
    }

    public void updateHomeScreen() {
        view.updateHomeScreen();
    }

    private void handleAddPlanner(String groupName) {
        CreatePlanPopup popup = new CreatePlanPopup();
        popup.setPlanCreationListener(planName -> {
            if (planName != null && !planName.isEmpty()) {
                try {
                    model.addPlannerToGroup(planName, groupName); // Modellhåndtering
                    view.updateHomeScreen(); // Oppdater visningen etter endring
                    System.out.println("Planner added: " + planName + " to group " + groupName);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error adding planner: " + e.getMessage());
                }
            } else {
                System.err.println("Planner name cannot be empty.");
            }
        });
        popup.showPopup();
    }

}
