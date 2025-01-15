package com.martin.dayplanner.view.views.homescreen;

import com.martin.dayplanner.view.views.PopupFieldKey;
import com.martin.dayplanner.view.views.PopupView;

public class HomeScreenPopupConfigurator {

    private final ViewableHomeScreen model;

    public HomeScreenPopupConfigurator(ViewableHomeScreen model) {
        this.model = model;
    }

    // Configure popup for creating a new group
    public PopupView configureCreateGroupPopup() {
        PopupView popup = new PopupView("Create New Group");
        popup.addNameField("Group Name:", "Enter group name", null);
        return popup;
    }

    // Configure popup for editing an existing group
    public PopupView configureEditGroupPopup(String groupId) {
        PopupView popup = new PopupView("Edit Group");
        popup.setMetadata(PopupFieldKey.ID, groupId);
        popup.addNameField("Group Name:", "Enter group name", model.getGroupNameById(groupId));
        return popup;
    }

    // Configure popup for removing a group with confirmation
    public PopupView configureRemoveGroupPopup(String groupId) {
        PopupView popup = new PopupView("Remove Group");
        popup.setMetadata(PopupFieldKey.ID, groupId);
        popup.addLabel("Are you sure you want to remove the group: \"" + model.getGroupNameById(groupId) + "\"?");
        return popup;
    }

    // Configure popup for creating a new plan
    public PopupView configureCreatePlanPopup() {
        PopupView popup = new PopupView("Create New Plan");
        popup.addNameField("Plan Name:", "Enter plan name", null);
        popup.addParentGroupComboBox("Group:", "select group", null, model.getPlannerGroups());
        popup.addDateTimePicker("Due Date:", "Due Time:", null, null);
        return popup;
    }

    // Configure popup for editing an existing plan
    public PopupView configureEditPlanPopup(String plannerId) {
        PopupView popup = new PopupView("Edit Plan");
        popup.setMetadata(PopupFieldKey.ID, plannerId);
        popup.addNameField("Plan Name:", "Enter plan name", model.getPlannerNameById(plannerId));
        popup.addParentGroupComboBox("Group:", "Select group", model.getParentGroupByPlannerId(plannerId),
                model.getPlannerGroups());
        popup.addDateTimePicker("Due Date:", "Due Time", model.getPlannerDate(plannerId),
                model.getPlannerTime(plannerId));
        return popup;
    }

    // Configure popup for removing a plan with confirmation
    public PopupView configureRemovePlanPopup(String plannerId) {
        PopupView popup = new PopupView("Remove Plan");
        popup.setMetadata(PopupFieldKey.ID, plannerId);
        popup.addLabel("Are you sure you want to remove the plan: \"" + model.getPlannerNameById(plannerId) + "\"?");
        return popup;
    }
}
