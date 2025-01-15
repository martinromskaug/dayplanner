package com.martin.dayplanner.model;

import com.martin.dayplanner.controller.ControllableAppModel;
import com.martin.dayplanner.model.storage.StorageHandler;
import com.martin.dayplanner.view.ViewableAppModel;

public class AppModel implements ViewableAppModel, ControllableAppModel {

    private final StorageHandler storageHandler;
    private HomeScreen homeScreenModel;
    private Object activeModel;
    private String activePlannerId;

    public AppModel() {
        this.storageHandler = new StorageHandler();
        this.homeScreenModel = new HomeScreen(this, storageHandler);
        this.activeModel = homeScreenModel;
        this.activePlannerId = null;
        setup();
    }

    private void setup() {
        for (Planner planner : storageHandler.getAllPlanners()) {
            planner.setStorageHandler(storageHandler);
            planner.setAppModel(this);
        }
    }

    @Override
    public Object getActiveModel() {
        return activeModel;
    }

    public void openPlanner(String plannerId) {
        Planner selectedPlanner = storageHandler.findPlannerByID(plannerId);
        if (selectedPlanner != null) {
            activeModel = selectedPlanner;
            activePlannerId = plannerId;
        }
    }

    public void goToMenu() {
        this.activeModel = homeScreenModel;
        this.activePlannerId = null;
    }

    @Override
    public HomeScreen getHomeScreenModel() {
        return homeScreenModel;
    }

    @Override
    public Planner getPlannerModel() {
        Planner plannerModel = storageHandler.findPlannerByID(activePlannerId);
        return plannerModel;
    }
}
