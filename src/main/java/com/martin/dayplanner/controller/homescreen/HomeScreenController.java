package com.martin.dayplanner.controller.homescreen;

import com.martin.dayplanner.controller.AppController;
import com.martin.dayplanner.view.views.ListItemData;
import com.martin.dayplanner.view.views.PopupFieldKey;
import com.martin.dayplanner.view.views.PopupView;
import com.martin.dayplanner.view.views.homescreen.HomeScreenPopupConfigurator;
import com.martin.dayplanner.view.views.homescreen.HomeScreenView;

import javafx.scene.control.TreeItem;

public class HomeScreenController {

    private final ControllableHomeScreen model;
    private final HomeScreenView view;
    private final AppController appController;
    private final HomeScreenPopupConfigurator popupConfigurator;

    public HomeScreenController(ControllableHomeScreen model, HomeScreenView view, AppController appController) {
        this.model = model;
        this.view = view;
        this.appController = appController;
        this.popupConfigurator = view.getPopup();

        setupEventHandlers();
    }

    private void setupEventHandlers() {
        view.getCreateNewPlanButton().setOnAction(e -> createNewPlan());
        view.getRemovePlanButton().setOnAction(e -> removeSelectedPlan());
        view.getEditPlanButton().setOnAction(e -> editSelectedPlan());
        view.getCreateNewGroupButton().setOnAction(e -> createNewGroup());
        view.getRemoveGroupButton().setOnAction(e -> removeSelectedGroup());
        view.getEditGroupButton().setOnAction(e -> editSelectedGroup());

        view.getPlansTreeView().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                goToSelectedPlan();
            }
        });
    }

    private void createNewPlan() {
        PopupView popup = popupConfigurator.configureCreatePlanPopup();

        popup.setActions(values -> {
            model.addPlanner(values);
            view.updateHomeScreen();
            System.out.printf("Plan created: %s in group ID: %s%n",
                    values.get(PopupFieldKey.NAME), values.get(PopupFieldKey.PARENT_GROUP));
        }, null);

        popup.show();
    }

    private void removeSelectedPlan() {
        TreeItem<ListItemData> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.isLeaf()) {
            String plannerId = selectedItem.getValue().getId();
            PopupView popup = popupConfigurator.configureRemovePlanPopup(plannerId);

            popup.setActions(values -> {
                model.removePlanner(plannerId);
                view.updateHomeScreen();
            }, null);

            popup.show();

            System.out.println("Plan removed with ID: " + plannerId);
        } else {
            System.out.println("No plan selected or selection is not a plan.");
        }
    }

    private void editSelectedPlan() {
        TreeItem<ListItemData> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.isLeaf()) {
            String plannerId = selectedItem.getValue().getId();
            PopupView popup = popupConfigurator.configureEditPlanPopup(plannerId);

            popup.setActions(values -> {
                model.editPlanner(values);
                view.updateHomeScreen();
                System.out.printf("Plan updated: ID %s -> %s%n",
                        plannerId, values.get(PopupFieldKey.NAME));
            }, null);

            popup.show();
        } else {
            System.out.println("No plan selected or selection is not a plan.");
        }
    }

    private void createNewGroup() {
        PopupView popup = popupConfigurator.configureCreateGroupPopup();

        popup.setActions(values -> {
            model.addPlannerGroup(values);
            view.updateHomeScreen();
            System.out.println("Group created: " + values.get(PopupFieldKey.NAME));
        }, null);

        popup.show();
    }

    private void removeSelectedGroup() {
        TreeItem<ListItemData> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null && !selectedItem.isLeaf()) {
            String groupId = selectedItem.getValue().getId();
            PopupView popup = popupConfigurator.configureRemoveGroupPopup(groupId);

            popup.setActions(values -> {
                model.removePlannerGroup(groupId);
                view.updateHomeScreen();
            }, null);

            popup.show();

            System.out.println("Group removed with ID: " + groupId);
        } else {
            System.out.println("No group selected or selection is not a group.");
        }
    }

    private void editSelectedGroup() {
        TreeItem<ListItemData> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null && !selectedItem.isLeaf()) {
            String groupId = selectedItem.getValue().getId();
            PopupView popup = popupConfigurator.configureEditGroupPopup(groupId);

            popup.setActions(values -> {
                model.editPlannerGroup(values);
                view.updateHomeScreen();
                System.out.printf("Group updated: ID %s -> %s%n", groupId, values.get(PopupFieldKey.NAME));
            }, null);

            popup.show();
        } else {
            System.out.println("No group selected or selection is not a group.");
        }
    }

    private void goToSelectedPlan() {
        TreeItem<ListItemData> selectedItem = view.getPlansTreeView().getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.isLeaf()) {
            String plannerId = selectedItem.getValue().getId();
            model.openPlanner(plannerId);
            appController.updateActiveView();
        } else {
            System.out.println("No plan selected or selection is not a plan.");
        }
    }

    public void updateHomeScreen() {
        view.updateHomeScreen();
    }
}
