package com.martin.dayplanner.model;

import com.martin.dayplanner.controller.planner.ControllablePlanner;
import com.martin.dayplanner.model.storage.StorageHandler;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskPriority;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.views.PopupFieldKey;
import com.martin.dayplanner.view.views.planner.ViewablePlanner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Planner implements ControllablePlanner, ViewablePlanner {

    private final String id;
    private String plannerName;
    private String groupId;
    private transient AppModel appModel;
    private transient StorageHandler storageHandler;
    private LocalDate dueDate;
    private LocalTime dueTime;

    public Planner(String plannerName, String groupId, StorageHandler storageHandler, AppModel appModel) {
        this.plannerName = plannerName;
        this.groupId = groupId;
        this.storageHandler = storageHandler;
        this.appModel = appModel;
        this.dueDate = null;
        this.dueTime = null;
        this.id = generateId();
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPlannerName() {
        return plannerName;
    }

    public void setPlannerName(String newPlannerName) {
        this.plannerName = newPlannerName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalTime dueTime) {
        this.dueTime = dueTime;
    }

    @Override
    public void goToMenu() {
        appModel.goToMenu();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Planner planner = (Planner) o;
        return Objects.equals(id, planner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setStorageHandler(StorageHandler storageHandler) {
        this.storageHandler = storageHandler;
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    @Override
    public String getTaskNameById(String taskId) {
        Task task = storageHandler.findTaskByID(taskId);
        if (task != null && task.getPlannerId().equals(this.id)) {
            return task.getTaskName();
        }
        return null;
    }

    @Override
    public LocalDate getTaskDateById(String taskId) {
        Task task = storageHandler.findTaskByID(taskId);
        if (task != null && task.getPlannerId().equals(this.id)) {
            return task.getDueDate();
        }
        return null;
    }

    @Override
    public LocalTime getTaskTimeById(String taskId) {
        Task task = storageHandler.findTaskByID(taskId);
        if (task != null && task.getPlannerId().equals(this.id)) {
            return task.getDueTime();
        }
        return null;
    }

    @Override
    public TaskPriority getTaskPriorityById(String taskId) {
        Task task = storageHandler.findTaskByID(taskId);
        if (task != null && task.getPlannerId().equals(this.id)) {
            return task.getPriority();
        }
        return null;
    }

    @Override
    public void addTask(Map<PopupFieldKey, Object> taskData) {
        String name = (String) taskData.get(PopupFieldKey.NAME);
        LocalDate dueDate = (LocalDate) taskData.get(PopupFieldKey.DUE_DATE);
        LocalTime dueTime = (LocalTime) taskData.get(PopupFieldKey.DUE_TIME);
        TaskPriority priority = (TaskPriority) taskData.get(PopupFieldKey.PRIORITY);

        if (name != null && priority != null) {
            Task newTask = new Task(name, this.id);
            newTask.setDueDate(dueDate);
            newTask.setDueTime(dueTime);
            newTask.setPriority(priority);
            storageHandler.addTaskToPlanner(newTask, this.id);
        } else {
            throw new IllegalArgumentException("Task must have a name and priority.");
        }
    }

    @Override
    public void removeTask(String taskId) {
        Task task = storageHandler.findTaskByID(taskId);
        if (task != null && task.getPlannerId().equals(this.id)) {
            storageHandler.removeTaskFromPlanner(taskId, this.id);
        } else {
            throw new IllegalArgumentException("Task not found or does not belong to this planner.");
        }
    }

    @Override
    public Task findTaskById(String taskId) {
        Task task = storageHandler.findTaskByID(taskId);
        if (task != null && task.getPlannerId().equals(this.id)) {
            return task;
        }
        return null;
    }

    @Override
    public void updateTaskStatus(String taskId, TaskStatus targetStatus) {
        Task task = storageHandler.findTaskByID(taskId);
        if (task != null && task.getPlannerId().equals(this.id)) {
            task.setStatus(targetStatus);
            storageHandler.updateTask(task);
        } else {
            throw new IllegalArgumentException("Task not found or does not belong to this planner.");
        }
    }

    @Override
    public void editTask(Map<PopupFieldKey, Object> taskData) {
        String taskId = (String) taskData.get(PopupFieldKey.ID);
        String name = (String) taskData.get(PopupFieldKey.NAME);
        LocalDate dueDate = (LocalDate) taskData.get(PopupFieldKey.DUE_DATE);
        LocalTime dueTime = (LocalTime) taskData.get(PopupFieldKey.DUE_TIME);
        TaskPriority priority = (TaskPriority) taskData.get(PopupFieldKey.PRIORITY);

        Task task = storageHandler.findTaskByID(taskId);
        if (task != null && task.getPlannerId().equals(this.id)) {
            task.setTaskName(name);
            task.setDueDate(dueDate);
            task.setDueTime(dueTime);
            task.setPriority(priority);
            storageHandler.updateTask(task);
        } else {
            throw new IllegalArgumentException("Task not found or does not belong to this planner.");
        }
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return storageHandler.getAllTasks().stream()
                .filter(task -> task.getPlannerId().equals(this.id) && task.getStatus() == status)
                .toList();
    }

    public double getProgress() {
        List<Task> tasksForPlanner = storageHandler.getAllTasks().stream()
                .filter(task -> task.getPlannerId().equals(this.id))
                .collect(Collectors.toList());

        if (tasksForPlanner.isEmpty()) {
            return 0.0;
        }

        long completedTasks = tasksForPlanner.stream()
                .filter(task -> task.getStatus().equals(TaskStatus.COMPLETED))
                .count();

        return (double) completedTasks / tasksForPlanner.size();
    }

}
