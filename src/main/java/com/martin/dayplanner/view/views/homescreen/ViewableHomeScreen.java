package com.martin.dayplanner.view.views.homescreen;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.model.task.Task;

public interface ViewableHomeScreen {

    public List<PlannerGroup> getPlannerGroups();

    public List<Planner> getPlannersForGroup(String groupId);

    public Map<String, List<Task>> getActiveTasks();

    public List<Planner> getPlannersWithDeadline();

    public String getPlannerNameById(String plannerId);

    public String getGroupNameById(String groupId);

    public PlannerGroup getParentGroupByPlannerId(String plannerId);

    public LocalDate getPlannerDate(String plannerId);

    public LocalTime getPlannerTime(String plannerId);
}
