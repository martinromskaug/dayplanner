package com.martin.dayplanner.model;

import java.util.ArrayList;
import java.util.List;

import com.martin.dayplanner.controller.homescreen.ControllableHomeScreen;
import com.martin.dayplanner.model.storage.StorageHandler;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.views.homescreen.ViewableHomeScreen;

public class HomeScreen implements ControllableHomeScreen, ViewableHomeScreen {

    private AppModel appModel;
    private final StorageHandler storageHandler;

    public HomeScreen(AppModel appModel, StorageHandler storageHandler) {
        this.appModel = appModel;
        this.storageHandler = storageHandler;
    }

    @Override
    public List<Planner> getPlanners() {
        List<String> plannerNames = storageHandler.getAllPlannerNames();
        List<Planner> planners = new ArrayList<>();
        for (String name : plannerNames) {
            planners.add(new Planner(name, appModel, storageHandler));
        }
        return planners;
    }

    @Override
    public void addPlanner(String plannerName) {
        if (plannerName == null || plannerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Planner name cannot be null or empty");
        }
        if (findPlannerByName(plannerName) != null) {
            throw new IllegalArgumentException("A planner with this name already exists");
        }

        storageHandler.addPlanner(plannerName);
    }

    @Override
    public void removePlanner(String plannerName) {
        Planner planner = findPlannerByName(plannerName);
        if (planner != null) {
            storageHandler.removePlanner(plannerName);
        }
    }

    public Planner findPlannerByName(String plannerName) {
        List<String> plannerNames = storageHandler.getAllPlannerNames();
        if (plannerNames.contains(plannerName)) {
            return new Planner(plannerName, appModel, storageHandler);
        }
        return null;
    }

    @Override
    public void openPlanner(String selectedPlanName) {
        appModel.openPlanner(selectedPlanName);
    }

    @Override
    public List<Task> getActiveTasks() {
        List<Task> activeTasks = new ArrayList<>();
        List<String> plannerNames = storageHandler.getAllPlannerNames();

        for (String plannerName : plannerNames) {
            List<Task> tasksForPlanner = storageHandler.getTasksForPlanner(plannerName);
            for (Task task : tasksForPlanner) {
                if (task.getStatus() == TaskStatus.ACTIVE) {
                    activeTasks.add(task);
                }
            }
        }

        return activeTasks;
    }

    @Override
    public List<Task> getTasksWithDeadline() {
        List<Task> tasksWithDeadline = new ArrayList<>();
        List<String> plannerNames = storageHandler.getAllPlannerNames();

        for (String plannerName : plannerNames) {
            List<Task> tasksForPlanner = storageHandler.getTasksForPlanner(plannerName);
            for (Task task : tasksForPlanner) {
                if (task.getDueDate() != null && task.getDueTime() != null) {
                    tasksWithDeadline.add(task);
                }
            }
        }

        return tasksWithDeadline;
    }

    @Override
    public void editPlanner(String selectedPlanName, String updatedPlanName) {
        if (updatedPlanName == null || updatedPlanName.trim().isEmpty()) {
            throw new IllegalArgumentException("Updated planner name cannot be null or empty.");
        }
        if (findPlannerByName(updatedPlanName) != null) {
            throw new IllegalArgumentException("A planner with the updated name already exists.");
        }

        Planner selectedPlanner = findPlannerByName(selectedPlanName);
        if (selectedPlanner == null) {
            throw new IllegalArgumentException("Planner not found: " + selectedPlanName);
        }

        // Bruk StorageHandler for å oppdatere planner-navnet
        storageHandler.updatePlannerName(selectedPlanName, updatedPlanName);
    }

}