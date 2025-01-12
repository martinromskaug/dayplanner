package com.martin.dayplanner.view.views.homescreen;

import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;

public class GroupTreeItem extends TreeItem<String> {
    private final String groupName;
    private final Button addButton;

    public GroupTreeItem(String groupName) {
        this.groupName = groupName;
        this.addButton = new Button("+");

        HBox content = new HBox(10);
        content.getChildren().addAll(new javafx.scene.control.Label(groupName), addButton);

        this.setValue(groupName); // Bruker gruppenavnet som verdi
    }

    public String getGroupName() {
        return groupName;
    }

    public Button getAddButton() {
        return addButton;
    }
}
