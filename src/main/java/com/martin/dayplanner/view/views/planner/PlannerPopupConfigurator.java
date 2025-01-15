package com.martin.dayplanner.view.views.planner;

import com.martin.dayplanner.view.views.PopupFieldKey;
import com.martin.dayplanner.view.views.PopupView;

public class PlannerPopupConfigurator {

    private final ViewablePlanner model;

    public PlannerPopupConfigurator(ViewablePlanner model) {
        this.model = model;
    }

    public PopupView createTaskPopup() {
        PopupView popup = new PopupView("Create New Task");
        popup.addNameField("taskName", "Enter task name", null);
        popup.addDateTimePicker("Due date:", "Due time:", null, null);
        popup.addPriorityComboBox("Priority:", "Select priority", null);

        return popup;
    }

    public PopupView editTaskPopup(String taskId) {
        PopupView popup = new PopupView("Edit Task");
        popup.setMetadata(PopupFieldKey.ID, taskId);
        popup.addNameField("Task name:", "Enter task name", model.getTaskNameById(taskId));
        popup.addDateTimePicker("Due date:", "Due time:", model.getTaskDateById(taskId), model.getTaskTimeById(taskId));
        popup.addPriorityComboBox("Priority:", "Select priority", model.getTaskPriorityById(taskId));
        return popup;
    }
}
