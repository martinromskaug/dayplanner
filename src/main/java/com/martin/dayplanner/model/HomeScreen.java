package com.martin.dayplanner.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Planner findPlannerByName(String plannerName) {
        if (plannerName == null || plannerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Planner name cannot be null or empty");
        }

        for (PlannerGroup group : storageHandler.getAllPlannerGroups()) {
            for (Planner planner : storageHandler.getPlannersForGroup(group)) {
                if (planner.getPlannerName().equals(plannerName)) {
                    return planner;
                }
            }
        }

        return null;
    }

    @Override
    public List<PlannerGroup> getPlannerGroups() {
        return storageHandler.getAllPlannerGroups();
    }

    @Override
    public List<Planner> getPlannersForGroup(PlannerGroup plannerGroup) {
        return storageHandler.getPlannersForGroup(plannerGroup);
    }

    @Override
    public void addPlannerGroup(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }
        storageHandler.addPlannerGroup(new PlannerGroup(groupName));
    }

    @Override
    public void removePlannerGroup(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }

        PlannerGroup groupToRemove = storageHandler.findGroupByName(groupName);

        storageHandler.removePlannerGroup(groupToRemove);
    }

    @Override
    public void removePlanner(String plannerToRemove) {
        if (plannerToRemove == null || plannerToRemove.trim().isEmpty()) {
            throw new IllegalArgumentException("Planner name cannot be null or empty");
        }

        PlannerGroup group = findPlannerByName(plannerToRemove).getPlannerGroup();

        Planner planner = storageHandler.getPlannersForGroup(group)
                .stream()
                .filter(p -> p.getPlannerName().equals(plannerToRemove))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Planner not found: " + plannerToRemove));

        storageHandler.removePlannerFromGroup(group, planner);
    }

    @Override
    public void openPlanner(String selectedPlanName) {
        appModel.openPlanner(selectedPlanName);
    }

    @Override
    public void editPlanner(String selectedPlanName, String updatedPlanName) {
        if (selectedPlanName == null || selectedPlanName.trim().isEmpty()) {
            throw new IllegalArgumentException("Selected planner name cannot be null or empty");
        }
        if (updatedPlanName == null || updatedPlanName.trim().isEmpty()) {
            throw new IllegalArgumentException("Updated planner name cannot be null or empty");
        }
        if (selectedPlanName.equals(updatedPlanName)) {
            throw new IllegalArgumentException("Updated planner name cannot be the same as the current name");
        }

        Planner plannerToEdit = findPlannerByName(selectedPlanName);
        if (plannerToEdit == null) {
            throw new IllegalArgumentException("Planner not found: " + selectedPlanName);
        }

        if (findPlannerByName(updatedPlanName) != null) {
            throw new IllegalArgumentException("A planner with the updated name already exists");
        }

        plannerToEdit.setPlannerName(updatedPlanName);

        storageHandler.updatePlanner(plannerToEdit.getPlannerGroup(), plannerToEdit);
    }

    @Override
    public void editPlannerGroup(String selectedGroupName, String updatedGroupName) {
        if (selectedGroupName == null || selectedGroupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Selected group name cannot be null or empty");
        }
        if (updatedGroupName == null || updatedGroupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Updated group name cannot be null or empty");
        }
        if (selectedGroupName.equals(updatedGroupName)) {
            throw new IllegalArgumentException("Updated group name cannot be the same as the current name");
        }

        PlannerGroup groupToEdit = storageHandler.findGroupByName(selectedGroupName);
        if (groupToEdit == null) {
            throw new IllegalArgumentException("PlannerGroup not found: " + selectedGroupName);
        }

        if (storageHandler.getAllPlannerGroups().stream()
                .anyMatch(group -> group.getGroupName().equals(updatedGroupName))) {
            throw new IllegalArgumentException("A PlannerGroup with the updated name already exists");
        }

        groupToEdit.setGroupName(updatedGroupName);

        storageHandler.updatePlannerGroup(groupToEdit);
    }

    @Override
    public Map<String, List<Task>> getActiveTasks() {
        return storageHandler.getAllTasks().stream()
                .filter(task -> task.getStatus() == TaskStatus.ACTIVE) // Kun aktive oppgaver
                .collect(Collectors.groupingBy(Task::getPlannerName)); // Gruppér etter planleggernavn
    }

    @Override
    public List<Planner> getPlannersWithDeadline() {
        return storageHandler.getAllPlanners().stream()
                .filter(planner -> planner.getDueDate() != null) // Sjekker om planlegger har en definert frist
                .toList();
    }

    @Override
    public void addPlannerToGroup(String groupName, String plannerName, LocalDate date, LocalTime time) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }
        if (plannerName == null || plannerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Planner name cannot be null or empty");
        }

        // Finn gruppen ved navn
        PlannerGroup group = storageHandler.findGroupByName(groupName);
        if (group == null) {
            throw new IllegalArgumentException("PlannerGroup not found: " + groupName);
        }

        // Sjekk om en planner med samme navn allerede finnes
        if (findPlannerByName(plannerName) != null) {
            throw new IllegalArgumentException("A planner with this name already exists");
        }

        // Opprett ny planner med dato og tid
        Planner newPlanner = new Planner(plannerName, group, appModel, storageHandler);
        newPlanner.setDueDate(date); // Sett dato
        newPlanner.setDueTime(time); // Sett klokkeslett

        // Legg til planner i gruppen via StorageHandler
        storageHandler.addPlannerToGroup(group, newPlanner);

        System.out.printf("Planner '%s' added to group '%s' with date %s and time %s%n",
                plannerName,
                groupName,
                date != null ? date.toString() : "No Date",
                time != null ? time.toString() : "No Time");
    }

}