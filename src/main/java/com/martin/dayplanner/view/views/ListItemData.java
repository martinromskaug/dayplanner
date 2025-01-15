package com.martin.dayplanner.view.views;

public class ListItemData {

    private final String id;
    private final String name;

    public ListItemData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
