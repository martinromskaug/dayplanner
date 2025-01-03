package com.martin.dayplanner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.martin.dayplanner.controller.ControllableDayPlanner;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.ViewableDayPlanner;

public class AppModel implements ControllableDayPlanner, ViewableDayPlanner {

    private List<Task> allTasks;

    public AppModel() {
        this.allTasks = new ArrayList<>();
        allTasks.add(new Task("Ringe Kasper"));
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks);
    }

    @Override
    public boolean addTask(Task task) {
        allTasks.add(task);
        return true;
    }

    @Override
    public boolean removeTask(String taskName) {

        Task selectedTask = findTaskByName(taskName);
        if (selectedTask != null) {
            allTasks.remove(selectedTask);
            return true;
        }
        return false;
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return allTasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Task findTaskByName(String selectedTaskName) {
        return allTasks.stream()
                .filter(task -> task.getName().equals(selectedTaskName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean editTask(String taskName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editTask'");
    }

    @Override
    public boolean completeTaskStep(String taskName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'completeTaskStep'");
    }

}
