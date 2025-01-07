package com.martin.dayplanner.view;

import com.martin.dayplanner.view.views.homescreen.ViewableHomeScreen;
import com.martin.dayplanner.view.views.planner.ViewablePlanner;

public interface ViewableAppModel {

    ViewableHomeScreen getHomeScreenModel();

    ViewablePlanner getPlannerModel();
}
