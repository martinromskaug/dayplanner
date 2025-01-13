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
    private final Map<String, Map<String, List<String>>> storage;

    public StorageHandler() {
        this.plannerGroups = new ArrayList<>();
        this.planners = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.storage = new HashMap<>();
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
                this.storage.putAll(Optional.ofNullable(data.getStorage()).orElse(new HashMap<>()));
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
            gson.toJson(new StorageData(plannerGroups, planners, tasks, storage), writer);
        } catch (IOException e) {
            System.err.println("Error saving storage: " + e.getMessage());
        }
    }

    public void addPlannerGroup(PlannerGroup groupToAdd) {
        if (!plannerGroups.contains(groupToAdd)) {
            plannerGroups.add(groupToAdd);
            storage.putIfAbsent(groupToAdd.getId(), new HashMap<>());
            saveStorage();
        }
    }

    public void addPlannerToGroup(PlannerGroup group, Planner plannerToAdd) {
        String groupId = group.getId();
        if (!plannerGroups.stream().anyMatch(g -> g.getId().equals(groupId))) {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }

        if (!planners.contains(plannerToAdd)) {
            planners.add(plannerToAdd);
            storage.get(groupId).putIfAbsent(plannerToAdd.getId(), new ArrayList<>());
            saveStorage();
        } else {
            throw new IllegalArgumentException("Planner already exists: " + plannerToAdd.getPlannerName());
        }
    }

    public void addTaskToPlanner(PlannerGroup group, Planner planner, Task taskToAdd) {
        String groupId = group.getId();
        String plannerId = planner.getId();

        if (!plannerGroups.stream().anyMatch(g -> g.getId().equals(groupId))) {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }

        if (!planners.stream().anyMatch(p -> p.getId().equals(plannerId))) {
            throw new IllegalArgumentException("Planner not found: " + planner.getPlannerName());
        }

        if (!tasks.contains(taskToAdd)) {
            tasks.add(taskToAdd);
        }

        storage.get(groupId).get(plannerId).add(taskToAdd.getId());
        saveStorage();
    }

    public List<PlannerGroup> getAllPlannerGroups() {
        return new ArrayList<>(plannerGroups);
    }

    public List<Planner> getPlannersForGroup(PlannerGroup group) {
        String groupId = group.getId();
        List<Planner> plannersForGroup = new ArrayList<>();
        for (Planner planner : planners) {
            if (planner.getGroupId().equals(groupId)) {
                plannersForGroup.add(planner);
            }
        }
        return plannersForGroup;
    }

    public List<Task> getTasksForPlanner(PlannerGroup group, Planner planner) {
        String groupId = group.getId();
        String plannerId = planner.getId();

        List<String> taskIds = storage.getOrDefault(groupId, Collections.emptyMap()).getOrDefault(plannerId,
                new ArrayList<>());
        List<Task> tasksForPlanner = new ArrayList<>();
        for (String taskId : taskIds) {
            Task task = tasks.stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
            tasksForPlanner.add(task);
        }
        return tasksForPlanner;
    }

    public void removePlannerGroup(PlannerGroup groupToRemove) {
        String groupId = groupToRemove.getId();
        if (storage.remove(groupId) != null) {
            plannerGroups.removeIf(group -> group.getId().equals(groupId));
            planners.removeIf(planner -> planner.getGroupId().equals(groupId));
            saveStorage();
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + groupToRemove.getGroupName());
        }
    }

    public void removePlannerFromGroup(PlannerGroup group, Planner plannerToRemove) {
        String groupId = group.getId();
        String plannerId = plannerToRemove.getId();

        Map<String, List<String>> plannerMap = storage.get(groupId);
        if (plannerMap != null && plannerMap.remove(plannerId) != null) {
            planners.removeIf(planner -> planner.getId().equals(plannerId));
            saveStorage();
        } else {
            throw new IllegalArgumentException("Planner not found in group: " + group.getGroupName());
        }
    }

    public void removeTaskFromPlanner(PlannerGroup group, Planner planner, Task taskToRemove) {
        String groupId = group.getId();
        String plannerId = planner.getId();
        String taskId = taskToRemove.getId();

        Map<String, List<String>> plannerMap = storage.get(groupId);
        if (plannerMap != null) {
            List<String> taskIds = plannerMap.get(plannerId);
            if (taskIds != null && taskIds.remove(taskId)) {
                tasks.removeIf(task -> task.getId().equals(taskId));
                saveStorage();
            } else {
                throw new IllegalArgumentException("Task not found in planner: " + planner.getPlannerName());
            }
        } else {
            throw new IllegalArgumentException("PlannerGroup not found: " + group.getGroupName());
        }
    }

    public void updatePlannerGroup(PlannerGroup groupToUpdate) {
        String groupId = groupToUpdate.getId();
        PlannerGroup existingGroup = plannerGroups.stream()
                .filter(group -> group.getId().equals(groupId))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("PlannerGroup not found: " + groupToUpdate.getGroupName()));

        plannerGroups.remove(existingGroup);
        plannerGroups.add(groupToUpdate);
        saveStorage();
    }

    public void updatePlanner(PlannerGroup group, Planner plannerToUpdate) {
        String plannerId = plannerToUpdate.getId();
        Planner existingPlanner = planners.stream()
                .filter(planner -> planner.getId().equals(plannerId))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Planner not found: " + plannerToUpdate.getPlannerName()));

        planners.remove(existingPlanner);
        planners.add(plannerToUpdate);
        saveStorage();
    }

    public void updateTask(PlannerGroup group, Planner planner, Task taskToUpdate) {
        String taskId = taskToUpdate.getId();
        Task existingTask = tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskToUpdate.getTaskName()));

        tasks.remove(existingTask);
        tasks.add(taskToUpdate);
        saveStorage();
    }

    public PlannerGroup findGroupByName(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }

        return plannerGroups.stream()
                .filter(group -> group.getGroupName().equalsIgnoreCase(groupName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PlannerGroup not found with name: " + groupName));
    }

    private static class StorageData {
        private final List<PlannerGroup> plannerGroups;
        private final List<Planner> planners;
        private final List<Task> tasks;
        private final Map<String, Map<String, List<String>>> storage;

        public StorageData(List<PlannerGroup> plannerGroups, List<Planner> planners, List<Task> tasks,
                Map<String, Map<String, List<String>>> storage) {
            this.plannerGroups = plannerGroups;
            this.planners = planners;
            this.tasks = tasks;
            this.storage = storage;
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

        public Map<String, Map<String, List<String>>> getStorage() {
            return storage;
        }
    }

    public List<Planner> getAllPlanners() {
        return planners;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public PlannerGroup findGroupByID(String groupId) {
        if (groupId == null || groupId.trim().isEmpty()) {
            throw new IllegalArgumentException("Group ID cannot be null or empty");
        }

        return plannerGroups.stream()
                .filter(group -> group.getId().equals(groupId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PlannerGroup not found with ID: " + groupId));
    }

}
