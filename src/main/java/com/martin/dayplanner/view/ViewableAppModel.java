package com.martin.dayplanner.view;

import com.martin.dayplanner.view.homescreen.ViewableHomeScreen;
import com.martin.dayplanner.view.planner.ViewablePlanner;

public interface ViewableAppModel {

    ViewableHomeScreen getHomeScreenModel();

    ViewablePlanner getPlannerModel();
}
