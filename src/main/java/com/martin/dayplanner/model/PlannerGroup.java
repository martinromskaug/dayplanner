package com.martin.dayplanner.model;

import java.util.Objects;
import java.util.UUID;

public class PlannerGroup {

    private final String Id;
    private String groupName;

    public PlannerGroup(String groupName) {
        this.groupName = groupName;
        this.Id = generateId();
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return Id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PlannerGroup that = (PlannerGroup) o;
        return Objects.equals(Id, that.Id); // Sammenlign ID-er
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id); // Basert på ID
    }

    @Override
    public String toString() {
        return groupName;
    }
}
