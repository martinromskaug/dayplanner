package com.martin.dayplanner.model.storage;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskPriority;
import com.martin.dayplanner.model.task.TaskStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class StorageHandlerTest {

    public static void main(String[] args) {
        StorageHandler storageHandler = new StorageHandler();

        // Test: Legge til en ny planlegger
        System.out.println("Legger til ny planlegger...");
        storageHandler.addPlanner("Planner1");
        System.out.println("Planleggere: " + storageHandler.getAllPlannerNames());

        // Test: Legge til oppgaver i planleggeren
        System.out.println("Legger til oppgaver...");
        Task task1 = new Task("Task1", "Planner1");
        task1.setDueDate(LocalDate.now());
        task1.setDueTime(LocalTime.now());
        task1.setPriority(TaskPriority.HIGH);
        task1.setStatus(TaskStatus.NOTSTARTED);
        storageHandler.addTaskToPlanner("Planner1", task1);

        // Test: Hente oppgaver for planleggeren
        List<Task> tasks = storageHandler.getTasksForPlanner("Planner1");
        System.out.println("Oppgaver i Planner1: " + tasks);

        // Test: Fjerne en oppgave
        System.out.println("Fjerner oppgave...");
        storageHandler.removeTaskFromPlanner("Planner1", "Task1");
        tasks = storageHandler.getTasksForPlanner("Planner1");
        System.out.println("Oppgaver i Planner1 etter fjerning: " + tasks);

        // Test: Lagre og laste planleggere
        System.out.println("Tester lagring og lasting...");
        storageHandler.savePlannersWithTasks();
        StorageHandler newStorageHandler = new StorageHandler();
        System.out.println("Planleggere etter lasting: " + newStorageHandler.getAllPlannerNames());
    }
}
