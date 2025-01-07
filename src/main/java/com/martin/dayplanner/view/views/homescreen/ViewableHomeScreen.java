package com.martin.dayplanner.view.views.homescreen;

import java.util.List;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.task.Task;

public interface ViewableHomeScreen {

    public List<Planner> getPlanners();

    public List<Task> getActiveTasks();

}
