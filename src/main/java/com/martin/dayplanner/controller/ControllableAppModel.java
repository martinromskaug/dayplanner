package com.martin.dayplanner.controller;

public interface ControllableAppModel {

    public ControllableHomeScreen getHomeScreenModel();

    public ControllableDayPlanner getPlannerModel();

    public Object getActiveModel();

}
