package com.martin.dayplanner.view.views;

public class ListItemData {

    private final String id;
    private final String name;
    private final Specimen specimen;

    public ListItemData(String id, String name, Specimen specimen) {
        this.id = id;
        this.name = name;
        this.specimen = specimen;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Specimen getSpecimen() {
        return specimen;
    }

    @Override
    public String toString() {
        return name;
    }
}
