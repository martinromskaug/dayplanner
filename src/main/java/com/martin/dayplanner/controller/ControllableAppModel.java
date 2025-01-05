package com.martin.dayplanner.controller;

import com.martin.dayplanner.model.HomeScreen;
import com.martin.dayplanner.model.Planner;

public interface ControllableAppModel {

    public ControllableHomeScreen getHomeScreenModel();

    public ControllableDayPlanner getPlannerModel();

}
