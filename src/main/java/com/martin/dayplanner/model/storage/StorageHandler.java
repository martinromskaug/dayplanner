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
import java.util.List;
import java.util.stream.Collectors;

public class StorageHandler {

    private static final String TASKS_FILE_PATH = "tasks.json";
    private static final String PLANNERS_FILE_PATH = "planners.json";

    private List<Task> allTasks; // Holder oppgavene i minnet

    public StorageHandler() {
        this.allTasks = loadTasks(); // Last opp oppgaver ved oppstart
    }

    public List<Task> loadTasks() {
        try (FileReader reader = new FileReader(TASKS_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            Type taskListType = new TypeToken<List<Task>>() {
            }.getType();
            List<Task> tasks = gson.fromJson(reader, taskListType);
            return tasks != null ? tasks : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Task> loadTasksForPlanner(String plannerName) {
        return allTasks.stream()
                .filter(task -> plannerName.equals(task.getPlannerName()))
                .collect(Collectors.toList());
    }

    public void saveTask(Task task) {
        // Fjern eventuelle eksisterende oppgaver med samme navn og planlegger
        allTasks.removeIf(
                t -> t.getTaskName().equals(task.getTaskName()) && t.getPlannerName().equals(task.getPlannerName()));
        allTasks.add(task); // Legg til eller oppdater oppgaven
        persistTasks();
    }

    public void removeTask(Task task) {
        allTasks.removeIf(
                t -> t.getTaskName().equals(task.getTaskName()) && t.getPlannerName().equals(task.getPlannerName()));
        persistTasks();
    }

    private void persistTasks() {
        try (FileWriter writer = new FileWriter(TASKS_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            gson.toJson(allTasks, writer);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    public List<String> loadPlannerNames() {
        try (FileReader reader = new FileReader(PLANNERS_FILE_PATH)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            List<String> plannerNames = gson.fromJson(reader, listType);
            return plannerNames != null ? plannerNames : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading planner names: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void savePlannerNames(List<String> plannerNames) {
        try (FileWriter writer = new FileWriter(PLANNERS_FILE_PATH)) {
            Gson gson = new Gson();
            gson.toJson(plannerNames, writer);
        } catch (IOException e) {
            System.err.println("Error saving planner names: " + e.getMessage());
        }
    }
}
