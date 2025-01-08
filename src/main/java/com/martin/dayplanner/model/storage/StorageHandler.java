package com.martin.dayplanner.model.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

    private Map<String, List<Task>> planners;

    public StorageHandler() {
        this.planners = loadPlannersWithTasks();
    }

    private Map<String, List<Task>> loadPlannersWithTasks() {
        try (FileReader reader = new FileReader(PLANNERS_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            Type mapType = new TypeToken<Map<String, List<Task>>>() {
            }.getType();
            Map<String, List<Task>> loadedPlanners = gson.fromJson(reader, mapType);
            return loadedPlanners != null ? loadedPlanners : new HashMap<>();
        } catch (IOException e) {
            System.err.println("Error loading planners and tasks: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public void savePlannersWithTasks() {
        try (FileWriter writer = new FileWriter(PLANNERS_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            gson.toJson(planners, writer);
        } catch (IOException e) {
            System.err.println("Error saving planners and tasks: " + e.getMessage());
        }
    }

    public List<Task> getTasksForPlanner(String plannerName) {
        return planners.getOrDefault(plannerName, new ArrayList<>());
    }

    public void addTaskToPlanner(String plannerName, Task task) {
        planners.putIfAbsent(plannerName, new ArrayList<>());
        planners.get(plannerName).add(task);
        savePlannersWithTasks();
    }

    public void removeTaskFromPlanner(String plannerName, String taskName) {
        List<Task> tasks = planners.get(plannerName);
        if (tasks != null) {
            tasks.removeIf(task -> task.getTaskName().equals(taskName));
            savePlannersWithTasks();
        }
    }

    public List<String> getAllPlannerNames() {
        return new ArrayList<>(planners.keySet());
    }

    public void addPlanner(String plannerName) {
        planners.putIfAbsent(plannerName, new ArrayList<>());
        savePlannersWithTasks();
    }

    public void removePlanner(String plannerName) {
        planners.remove(plannerName);
        savePlannersWithTasks();
    }

    public void updateTask(Task task) {
        List<Task> tasks = planners.get(task.getPlannerName());
        if (tasks != null) {
            tasks.removeIf(t -> t.getTaskName().equals(task.getTaskName()));
            tasks.add(task);
            savePlannersWithTasks();
        }
    }
}
