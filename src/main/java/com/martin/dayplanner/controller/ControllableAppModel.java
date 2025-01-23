package com.martin.dayplanner.controller;

import com.martin.dayplanner.controller.homescreen.ControllableHomeScreen;
import com.martin.dayplanner.controller.planner.ControllablePlanner;

public interface ControllableAppModel {

    /**
     * Returns the home screen model.
     * 
     * @return the home screen model
     */
    public ControllableHomeScreen getHomeScreenModel();

    /**
     * Returns the planner model.
     * 
     * @return the planner model
     */
    public ControllablePlanner getPlannerModel();

    /**
     * Returns the active model.
     * 
     * @return the active model
     */
    public Object getActiveModel();

}
