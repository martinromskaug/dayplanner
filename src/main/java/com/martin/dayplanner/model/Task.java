package com.martin.dayplanner.model;

public class Task {

    private String name;
    private boolean isCompleted;

    public Task(String name) {
        this.name = name;
        this.isCompleted = false;
    }

    public Object getName() {
        return name;
    }

}
