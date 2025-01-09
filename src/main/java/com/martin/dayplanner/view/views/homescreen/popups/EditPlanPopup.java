package com.martin.dayplanner.view.views.homescreen.popups;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class EditPlanPopup {

    private final Stage stage;
    private final TextField planNameField;
    private PlanEditListener planEditListener;

    public EditPlanPopup(String initialPlanName) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Plan");

        planNameField = new TextField(initialPlanName); // Sett inn eksisterende navn
        planNameField.setPromptText("Edit plan name");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if (planEditListener != null) {
                planEditListener.onPlanEdited(planNameField.getText().trim());
            }
            stage.close();
        });

        VBox layout = new VBox(10, planNameField, saveButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        stage.setScene(new Scene(layout, 300, 150));
    }

    public void setPlanEditListener(PlanEditListener listener) {
        this.planEditListener = listener;
    }

    public void showPopup() {
        stage.showAndWait();
    }

    public interface PlanEditListener {
        void onPlanEdited(String updatedPlanName);
    }
}
