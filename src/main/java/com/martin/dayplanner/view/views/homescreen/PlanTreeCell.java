package com.martin.dayplanner.view.views.homescreen;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;

public class PlanTreeCell extends TreeCell<String> {
    private final HBox content;
    private final Label label;
    private final Button addButton;

    public PlanTreeCell() {
        label = new Label();
        addButton = new Button("+");

        // Konfigurer handling for "Legg til"-knappen
        addButton.setOnAction(event -> {
            if (getTreeItem() != null && getTreeItem().getParent() == null) {
                System.out.println("Add clicked for group: " + getTreeItem().getValue());
                // Her kan du legge til logikken for å legge til en ny plan i gruppen
            }
        });

        // Sett opp layout for cellen
        content = new HBox(10, label, addButton);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            label.setText(item);

            // Skjul "Legg til"-knappen for planer (kun vis den for grupper)
            if (getTreeItem() != null && getTreeItem().getParent() == null) {
                setGraphic(content);
            } else {
                setGraphic(label);
            }

            setText(null); // Teksten settes til `null` for å unngå duplikater
        }
    }
}
