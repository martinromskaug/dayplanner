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
import java.util.*;

public class StorageHandler {

    private static final String STORAGE_FILE_PATH = "data/planners.json";

    private final List<PlannerGroup> plannerGroups;
    private final List<Planner> planners;
    private final List<Task> tasks;
    private final Map<String, Map<String, List<String>>> relations;

    public StorageHandler() {
        this.plannerGroups = new ArrayList<>();
        this.planners = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.relations = new HashMap<>();
        loadStorage();
    }

    private void loadStorage() {
        try (FileReader reader = new FileReader(STORAGE_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .setPrettyPrinting()
                    .create();
            Type type = new TypeToken<StorageData>() {
            }.getType();
            StorageData data = gson.fromJson(reader, type);
            if (data != null) {
                this.plannerGroups.addAll(Optional.ofNullable(data.getPlannerGroups()).orElse(new ArrayList<>()));
                this.planners.addAll(Optional.ofNullable(data.getPlanners()).orElse(new ArrayList<>()));
                this.tasks.addAll(Optional.ofNullable(data.getTasks()).orElse(new ArrayList<>()));
                this.relations.putAll(Optional.ofNullable(data.getRelations()).orElse(new HashMap<>()));
            }
        } catch (IOException e) {
            System.err.println("Error loading storage: " + e.getMessage());
        }
    }

    public void saveStorage() {
        try (FileWriter writer = new FileWriter(STORAGE_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .setPrettyPrinting()
                    .create();
            gson.toJson(new StorageData(plannerGroups, planners, tasks, relations), writer);
        } catch (IOException e) {
            System.err.println("Error saving storage: " + e.getMessage());
        }
    }

    // --- CRUD Operations ---

    public void addPlannerGroup(PlannerGroup groupToAdd) {
        String groupId = groupToAdd.getId();
        if (plannerGroups.stream().noneMatch(g -> g.getId().equals(groupId))) {
            plannerGroups.add(groupToAdd);
            relations.putIfAbsent(groupId, new HashMap<>());
            saveStorage();
        } else {
            throw new IllegalArgumentException("PlannerGroup already exists: " + groupToAdd.getGroupName());
        }
    }

    public void updatePlannerGroup(PlannerGroup updatedGroup) {
        String groupId = updatedGroup.getId();
        PlannerGroup existingGroup = findGroupByID(groupId);
        plannerGroups.remove(existingGroup);
        plannerGroups.add(updatedGroup);
        saveStorage();
    }

    public void addPlannerToGroup(Planner plannerToAdd, String groupId) {
        if (!relations.containsKey(groupId)) {
            throw new IllegalArgumentException("PlannerGroup not found with ID: " + groupId);
        }

        if (planners.stream().noneMatch(p -> p.getId().equals(plannerToAdd.getId()))) {
            planners.add(plannerToAdd);
            relations.get(groupId).putIfAbsent(plannerToAdd.getId(), new ArrayList<>());
            saveStorage();
        } else {
            throw new IllegalArgumentException("Planner already exists: " + plannerToAdd.getPlannerName());
        }
    }

    public void updatePlanner(Planner updatedPlanner) {
        String plannerId = updatedPlanner.getId();
        Planner existingPlanner = findPlannerByID(plannerId);
        planners.remove(existingPlanner);
        planners.add(updatedPlanner);
        saveStorage();
    }

    public void addTaskToPlanner(Task taskToAdd, String plannerId) {
        Planner planner = findPlannerByID(plannerId); // Sjekker at planner finnes
        String groupId = planner.getGroupId(); // Henter tilknyttet gruppe

        if (!relations.containsKey(groupId)) {
            throw new IllegalArgumentException("PlannerGroup not found with ID: " + groupId);
        }

        if (tasks.stream().noneMatch(t -> t.getId().equals(taskToAdd.getId()))) {
            tasks.add(taskToAdd);
        }

        Map<String, List<String>> plannerMap = relations.get(groupId);
        if (plannerMap != null) {
            plannerMap.computeIfAbsent(plannerId, _ -> new ArrayList<>()).add(taskToAdd.getId());
            saveStorage();
        } else {
            throw new IllegalArgumentException("Planner not associated with any group: " + plannerId);
        }
    }

    public void updateTask(Task updatedTask) {
        String taskId = updatedTask.getId();
        Task existingTask = findTaskByID(taskId);
        tasks.remove(existingTask);
        tasks.add(updatedTask);
        saveStorage();
    }

    // --- Retrieval Methods ---

    public List<PlannerGroup> getAllPlannerGroups() {
        return new ArrayList<>(plannerGroups);
    }

    public List<Planner> getAllPlanners() {
        return new ArrayList<>(planners);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Map<String, Map<String, List<String>>> getRelations() {
        return new HashMap<String, Map<String, List<String>>>(relations);
    }

    public PlannerGroup findGroupByID(String groupId) {
        return plannerGroups.stream()
                .filter(group -> group.getId().equals(groupId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PlannerGroup not found with ID: " + groupId));
    }

    public Planner findPlannerByID(String plannerId) {
        return planners.stream()
                .filter(planner -> planner.getId().equals(plannerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Planner not found with ID: " + plannerId));
    }

    public Task findTaskByID(String taskId) {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
    }

    // --- Deletion Methods ---

    public void removePlannerGroup(String groupId) {
        if (relations.remove(groupId) != null) {
            tasks.removeIf(task -> findPlannerByID(task.getPlannerId()).getGroupId().equals(groupId));
            planners.removeIf(planner -> planner.getGroupId().equals(groupId));
            plannerGroups.removeIf(group -> group.getId().equals(groupId));
            saveStorage();
        } else {
            throw new IllegalArgumentException("PlannerGroup not found with ID: " + groupId);
        }
    }

    public void removePlannerFromGroup(String plannerId, String groupId) {
        Map<String, List<String>> plannerMap = relations.get(groupId);
        if (plannerMap != null && plannerMap.remove(plannerId) != null) {
            tasks.removeIf(task -> task.getPlannerId().equals(plannerId));
            planners.removeIf(planner -> planner.getId().equals(plannerId));
            saveStorage();
        } else {
            throw new IllegalArgumentException("Planner not found in group: " + groupId);
        }
    }

    public void removeTaskFromPlanner(String taskId, String plannerId) {
        for (Map<String, List<String>> plannerMap : relations.values()) {
            if (plannerMap.containsKey(plannerId)) {
                List<String> taskIds = plannerMap.get(plannerId);
                if (taskIds.remove(taskId)) {
                    tasks.removeIf(task -> task.getId().equals(taskId));
                    saveStorage();
                    return;
                }
            }
        }

        throw new IllegalArgumentException("Task not found in planner: " + plannerId);
    }

    // --- Inner Classes ---

    private static class StorageData {
        private final List<PlannerGroup> plannerGroups;
        private final List<Planner> planners;
        private final List<Task> tasks;
        private final Map<String, Map<String, List<String>>> relations;

        public StorageData(List<PlannerGroup> plannerGroups, List<Planner> planners, List<Task> tasks,
                Map<String, Map<String, List<String>>> relations) {
            this.plannerGroups = plannerGroups;
            this.planners = planners;
            this.tasks = tasks;
            this.relations = relations;
        }

        public List<PlannerGroup> getPlannerGroups() {
            return plannerGroups;
        }

        public List<Planner> getPlanners() {
            return planners;
        }

        public List<Task> getTasks() {
            return tasks;
        }

        public Map<String, Map<String, List<String>>> getRelations() {
            return relations;
        }
    }

    public Object getGroupIdForTask(String id) {
        return null;
    }
}
