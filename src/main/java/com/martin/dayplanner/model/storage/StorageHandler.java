package com.martin.dayplanner.model.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.model.task.Task;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageHandler {

    private static final String PLANNERS_FILE_PATH = "data/planners.json";

    private Map<PlannerGroup, Map<Planner, List<Task>>> storage;

    public StorageHandler() {
        this.storage = loadStorage();
    }

    private Map<PlannerGroup, Map<Planner, List<Task>>> loadStorage() {
        try (FileReader reader = new FileReader(PLANNERS_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            Type storageType = new TypeToken<Map<PlannerGroup, Map<Planner, List<Task>>>>() {
            }.getType();
            Map<PlannerGroup, Map<Planner, List<Task>>> loadedStorage = gson.fromJson(reader, storageType);
            return loadedStorage != null ? loadedStorage : new HashMap<>();
        } catch (IOException e) {
            System.err.println("Error loading storage: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public void saveStorage() {
        try (FileWriter writer = new FileWriter(PLANNERS_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            gson.toJson(storage, writer);
        } catch (IOException e) {
            System.err.println("Error saving storage: " + e.getMessage());
        }
    }

    public void addPlannerGroup(PlannerGroup groupToAdd) {
        storage.putIfAbsent(groupToAdd, new HashMap<>());
        saveStorage();
    }

    public void addPlannerToGroup(PlannerGroup group, Planner plannerToAdd) {
        Map<Planner, List<Task>> planners = storage.get(group);
        if (planners != null) {
            planners.putIfAbsent(plannerToAdd, new ArrayList<>());
            saveStorage();
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public void addTaskToPlanner(PlannerGroup group, Planner planner, Task taskToAdd) {
        Map<Planner, List<Task>> planners = storage.get(group);
        if (planners != null) {
            planners.putIfAbsent(planner, new ArrayList<>());
            planners.get(planner).add(taskToAdd);
            saveStorage();
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public List<PlannerGroup> getAllPlannerGroups() {
        return new ArrayList<>(storage.keySet());
    }

    public List<Planner> getPlannersForGroup(PlannerGroup group) {
        Map<Planner, List<Task>> planners = storage.get(group);
        if (planners != null) {
            return new ArrayList<>(planners.keySet());
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public List<Task> getTasksForPlanner(PlannerGroup group, Planner planner) {
        Map<Planner, List<Task>> planners = storage.get(group);
        if (planners != null) {
            return planners.getOrDefault(planner, new ArrayList<>());
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public void removePlannerGroup(PlannerGroup groupToRemove) {
        if (storage.remove(groupToRemove) != null) {
            saveStorage();
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + groupToRemove.getGroupName());
        }
    }

    public void removePlannerFromGroup(PlannerGroup group, Planner plannerToRemove) {
        Map<Planner, List<Task>> planners = storage.get(group);
        if (planners != null) {
            if (planners.remove(plannerToRemove) != null) {
                saveStorage();
            } else {
                throw new IllegalArgumentException("Planner not found in group: " + group.getGroupName());
            }
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public void removeTaskFromPlanner(PlannerGroup group, Planner planner, Task taskToRemove) {
        Map<Planner, List<Task>> planners = storage.get(group);
        if (planners != null) {
            List<Task> tasks = planners.get(planner);
            if (tasks != null && tasks.remove(taskToRemove)) {
                saveStorage();
            } else {
                throw new IllegalArgumentException("Task not found in planner: " + planner.getPlannerName());
            }
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public void updatePlannerGroup(PlannerGroup groupToUpdate) {
        Map<Planner, List<Task>> planners = storage.remove(groupToUpdate);
        if (planners != null) {
            storage.put(groupToUpdate, planners);
            saveStorage();
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + groupToUpdate.getGroupName());
        }
    }

    public void updatePlanner(PlannerGroup group, Planner plannerToUpdate) {
        Map<Planner, List<Task>> planners = storage.get(group);
        if (planners != null) {
            List<Task> tasks = planners.remove(plannerToUpdate);
            planners.put(plannerToUpdate, tasks != null ? tasks : new ArrayList<>());
            saveStorage();
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public void updateTask(PlannerGroup group, Planner planner, Task taskToUpdate) {
        Map<Planner, List<Task>> planners = storage.get(group);
        if (planners != null) {
            List<Task> tasks = planners.get(planner);
            if (tasks != null) {
                tasks.removeIf(task -> task.getTaskName().equals(taskToUpdate.getTaskName()));
                tasks.add(taskToUpdate);
                saveStorage();
            } else {
                throw new IllegalArgumentException("Planner not found: " + planner.getPlannerName());
            }
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public PlannerGroup findGroupByName(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }

        return storage.keySet()
                .stream()
                .filter(group -> group.getGroupName().equals(groupName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PlannerGroup not found: " + groupName));
    }

}
