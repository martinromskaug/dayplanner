package com.martin.dayplanner.model;

import java.util.ArrayList;
import java.util.List;

import com.martin.dayplanner.controller.ControllableHomeScreen;
import com.martin.dayplanner.view.ViewableHomeScreen;

public class HomeScreen implements ControllableHomeScreen, ViewableHomeScreen {

    private final List<Planner> planners;

    public HomeScreen() {
        this.planners = new ArrayList<>();
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
        planners.add(new Planner(plannerName));
    }

    @Override
    public void removePlanner(String plannerName) {
        Planner planner = findPlannerByName(plannerName);
        if (planner != null) {
            planners.remove(planner);
        }
    }

    public Planner findPlannerByName(String plannerName) {
        return planners.stream()
                .filter(planner -> planner.getPlannerName().equalsIgnoreCase(plannerName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void openPlanner(String selectedPlanName) {
        System.out.println("not implemented");
    }
}
