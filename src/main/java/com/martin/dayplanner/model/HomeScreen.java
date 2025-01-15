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
import com.martin.dayplanner.view.views.PopupFieldKey;
import com.martin.dayplanner.view.views.homescreen.ViewableHomeScreen;

public class HomeScreen implements ControllableHomeScreen, ViewableHomeScreen {

    private final AppModel appModel;
    private final StorageHandler storageHandler;

    public HomeScreen(AppModel appModel, StorageHandler storageHandler) {
        this.appModel = appModel;
        this.storageHandler = storageHandler;
    }

    @Override
    public List<PlannerGroup> getPlannerGroups() {
        return storageHandler.getAllPlannerGroups();
    }

    @Override
    public List<Planner> getPlannersForGroup(String groupId) {
        return storageHandler.getAllPlanners().stream()
                .filter(planner -> planner.getGroupId().equals(groupId))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Task>> getActiveTasks() {
        return storageHandler.getAllTasks().stream()
                .filter(task -> task.getStatus() == TaskStatus.ACTIVE)
                .collect(Collectors.groupingBy(Task::getPlannerId));
    }

    @Override
    public List<Planner> getPlannersWithDeadline() {
        return storageHandler.getAllPlanners().stream()
                .filter(planner -> planner.getDueDate() != null)
                .collect(Collectors.toList());
    }

    @Override
    public String getPlannerNameById(String plannerId) {
        Planner planner = storageHandler.findPlannerByID(plannerId);
        return planner != null ? planner.getPlannerName() : null;
    }

    @Override
    public String getGroupNameById(String groupId) {
        PlannerGroup group = storageHandler.findGroupByID(groupId);
        return group != null ? group.getGroupName() : null;
    }

    @Override
    public PlannerGroup getParentGroupByPlannerId(String plannerId) {
        Planner planner = storageHandler.findPlannerByID(plannerId);
        if (planner != null) {
            return storageHandler.findGroupByID(planner.getGroupId());
        }
        return null;
    }

    @Override
    public LocalDate getPlannerDate(String plannerId) {
        Planner planner = storageHandler.findPlannerByID(plannerId);
        return planner != null ? planner.getDueDate() : null;
    }

    @Override
    public LocalTime getPlannerTime(String plannerId) {
        Planner planner = storageHandler.findPlannerByID(plannerId);
        return planner != null ? planner.getDueTime() : null;
    }

    @Override
    public void addPlanner(Map<PopupFieldKey, Object> plannerData) {
        String name = (String) plannerData.get(PopupFieldKey.NAME);
        String parentId = (String) plannerData.get(PopupFieldKey.PARENT_GROUP);
        LocalDate dueDate = (LocalDate) plannerData.get(PopupFieldKey.DUE_DATE);
        LocalTime dueTime = (LocalTime) plannerData.get(PopupFieldKey.DUE_TIME);

        if (name == null || parentId == null) {
            throw new IllegalArgumentException("Planner must have a name and a parent group.");
        }

        // Valider at parentId eksisterer
        PlannerGroup parentGroup = storageHandler.findGroupByID(parentId);
        if (parentGroup == null) {
            throw new IllegalArgumentException("Parent group not found with ID: " + parentId);
        }

        // Opprett og legg til planner
        Planner newPlanner = new Planner(name, parentId, storageHandler, appModel);
        newPlanner.setDueDate(dueDate);
        newPlanner.setDueTime(dueTime);

        storageHandler.addPlannerToGroup(newPlanner, parentId);
    }

    @Override
    public void editPlanner(Map<PopupFieldKey, Object> plannerData) {
        String plannerId = (String) plannerData.get(PopupFieldKey.ID);
        String name = (String) plannerData.get(PopupFieldKey.NAME);
        LocalDate dueDate = (LocalDate) plannerData.get(PopupFieldKey.DUE_DATE);
        LocalTime dueTime = (LocalTime) plannerData.get(PopupFieldKey.DUE_TIME);

        Planner existingPlanner = storageHandler.findPlannerByID(plannerId);
        if (existingPlanner != null) {
            existingPlanner.setPlannerName(name);
            existingPlanner.setDueDate(dueDate);
            existingPlanner.setDueTime(dueTime);
            storageHandler.updatePlanner(existingPlanner);
        } else {
            throw new IllegalArgumentException("Planner not found with ID: " + plannerId);
        }
    }

    @Override
    public void addPlannerGroup(Map<PopupFieldKey, Object> groupData) {
        String name = (String) groupData.get(PopupFieldKey.NAME);
        if (name != null && !name.isEmpty()) {
            PlannerGroup newGroup = new PlannerGroup(name);
            storageHandler.addPlannerGroup(newGroup);
        } else {
            throw new IllegalArgumentException("Group must have a name.");
        }
    }

    @Override
    public void editPlannerGroup(Map<PopupFieldKey, Object> groupData) {
        String groupId = (String) groupData.get(PopupFieldKey.ID);
        String name = (String) groupData.get(PopupFieldKey.NAME);

        PlannerGroup group = storageHandler.findGroupByID(groupId);
        if (group != null) {
            group.setGroupName(name);
            storageHandler.updatePlannerGroup(group);
        } else {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }
    }

    @Override
    public void removePlannerGroup(String groupId) {
        storageHandler.removePlannerGroup(groupId);
    }

    @Override
    public void openPlanner(String plannerId) {
        appModel.openPlanner(plannerId);
    }

    @Override
    public void removePlanner(String plannerId) {
        Planner planner = storageHandler.findPlannerByID(plannerId);
        if (planner != null) {
            storageHandler.removePlannerFromGroup(plannerId, planner.getGroupId());
        } else {
            throw new IllegalArgumentException("Planner not found with ID: " + plannerId);
        }
    }

}