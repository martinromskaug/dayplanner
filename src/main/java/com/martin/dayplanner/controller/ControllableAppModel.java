package com.martin.dayplanner.controller;

import com.martin.dayplanner.controller.homescreen.ControllableHomeScreen;
import com.martin.dayplanner.controller.planner.ControllablePlanner;

public interface ControllableAppModel {

    public ControllableHomeScreen getHomeScreenModel();

    public ControllablePlanner getPlannerModel();

    public Object getActiveModel();

}
