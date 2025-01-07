package com.martin.dayplanner.model;

import java.util.ArrayList;
import java.util.List;

import com.martin.dayplanner.controller.homescreen.ControllableHomeScreen;
import com.martin.dayplanner.model.storage.StorageHandler;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.view.views.homescreen.ViewableHomeScreen;

public class HomeScreen implements ControllableHomeScreen, ViewableHomeScreen {

    private AppModel appModel;
    private List<Planner> planners;
    private final StorageHandler storageHandler;

    public HomeScreen(AppModel appModel) {
        this.appModel = appModel;
        this.storageHandler = new StorageHandler();
        this.planners = new ArrayList<>();

        List<String> plannerNames = storageHandler.loadPlannerNames();
        for (String name : plannerNames) {
            planners.add(new Planner(name, appModel));
        }
    }

    @Override
    public List<Planner> getPlanners() {
        return new ArrayList<>(planners);
    }

    @Override
    public void addPlanner(String plannerName) {
        if (plannerName == null || plannerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Planner name cannot be null or empty");
        }
        if (findPlannerByName(plannerName) != null) {
            throw new IllegalArgumentException("A planner with this name already exists");
        }
        planners.add(new Planner(plannerName, appModel));
        savePlannerNames();

    }

    @Override
    public void removePlanner(String plannerName) {
        Planner planner = findPlannerByName(plannerName);
        if (planner != null) {
            // Fjern alle oppgaver relatert til denne planleggeren
            List<Task> tasks = planner.getAllTasks();
            tasks.forEach(task -> planner.removeTask(task.getTaskName()));

            // Fjern selve planleggeren
            planners.remove(planner);

            // Oppdater lagringen
            savePlannerNames();
        }
    }

    private void savePlannerNames() {
        List<String> plannerNames = new ArrayList<>();
        for (Planner planner : planners) {
            plannerNames.add(planner.getPlannerName());
        }
        storageHandler.savePlannerNames(plannerNames);
    }

    public Planner findPlannerByName(String plannerName) {
        return planners.stream()
                .filter(planner -> planner.getPlannerName().equalsIgnoreCase(plannerName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void openPlanner(String selectedPlanName) {
        appModel.openPlanner(selectedPlanName);
    }

    public Planner getPlannerModel() {
        return null;
    }
}