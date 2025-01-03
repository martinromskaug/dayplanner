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

public class StorageHandler {

    private static final String FILE_PATH = "tasks.json";

    public List<Task> loadTasks() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            Type taskListType = new TypeToken<List<Task>>() {
            }.getType();
            return gson.fromJson(reader, taskListType);
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            saveTasks(new ArrayList<>()); // Opprett filen hvis den mangler
            return new ArrayList<>();
        }
    }

    public void saveTasks(List<Task> tasks) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

}
