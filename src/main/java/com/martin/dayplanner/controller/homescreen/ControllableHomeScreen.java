package com.martin.dayplanner.controller.homescreen;

public interface ControllableHomeScreen {

    public void addPlanner(String plannerName);

    public void removePlanner(String plannerName);

    public void openPlanner(String selectedPlanName);
}
