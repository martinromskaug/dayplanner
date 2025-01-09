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
        return groupName + "-" + UUID.randomUUID();
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
        return Objects.equals(groupName, that.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }
}
