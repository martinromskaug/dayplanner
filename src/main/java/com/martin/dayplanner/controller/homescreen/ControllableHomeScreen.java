package com.martin.dayplanner.controller.homescreen;

import java.util.Map;

import com.martin.dayplanner.view.views.PopupFieldKey;

public interface ControllableHomeScreen {

    void addPlanner(Map<PopupFieldKey, Object> plannerData);

    void editPlanner(Map<PopupFieldKey, Object> plannerData);

    void removePlanner(String plannerId);

    void addPlannerGroup(Map<PopupFieldKey, Object> groupData);

    void editPlannerGroup(Map<PopupFieldKey, Object> groupData);

    void removePlannerGroup(String groupId);

    void openPlanner(String plannerId);
}
