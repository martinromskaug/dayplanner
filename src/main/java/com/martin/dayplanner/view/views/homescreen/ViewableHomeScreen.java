package com.martin.dayplanner.view.views.homescreen;

import java.util.List;
import java.util.Map;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.model.task.Task;

public interface ViewableHomeScreen {

    public List<PlannerGroup> getPlannerGroups();

    public List<Planner> getPlannersForGroup(PlannerGroup plannerGroup);

    public Map<String, List<Task>> getActiveTasks();

    public List<Planner> getPlannersWithDeadline();
}
