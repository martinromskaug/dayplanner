package com.martin.dayplanner.view.views.homescreen.popups;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RemovePlanPopup {

    private boolean confirmed = false;

    public boolean showPopup(String planName) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Confirm Removal");

        // Opprett innholdet i popup-vinduet
        Label confirmationMessage = new Label("Are you sure you want to remove the plan: " + planName + "?");
        confirmationMessage.setWrapText(true);

        Button confirmButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        confirmButton.setOnAction(e -> {
            confirmed = true;
            popupStage.close();
        });

        cancelButton.setOnAction(e -> {
            confirmed = false;
            popupStage.close();
        });

        HBox buttonLayout = new HBox(10, confirmButton, cancelButton);
        VBox layout = new VBox(10, confirmationMessage, buttonLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait();

        return confirmed;
    }
}
