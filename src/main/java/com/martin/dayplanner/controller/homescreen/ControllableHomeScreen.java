package com.martin.dayplanner.controller.homescreen;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ControllableHomeScreen {

    public void addPlannerGroup(String groupName);

    public void addPlannerToGroup(String groupName, String plannerName, LocalDate date, LocalTime time);

    public void removePlannerGroup(String groupToRemove);

    public void removePlanner(String plannerToRemove);

    public void openPlanner(String selectedPlanName);

    public void editPlannerGroup(String selectedGroupName, String updatedGroupName);

    public void editPlanner(String selectedPlanName, String updatedPlanName);
}
