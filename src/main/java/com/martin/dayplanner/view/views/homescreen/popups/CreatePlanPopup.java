package com.martin.dayplanner.view.views.homescreen.popups;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class CreatePlanPopup {

    private final Stage stage;
    private final TextField planNameField;
    private PlanCreationListener planCreationListener;

    public CreatePlanPopup() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Create New Plan");

        planNameField = new TextField();
        planNameField.setPromptText("Enter plan name");

        Button createButton = new Button("Create");
        createButton.setOnAction(e -> {
            if (planCreationListener != null) {
                planCreationListener.onPlanCreated(planNameField.getText().trim());
            }
            stage.close();
        });

        VBox layout = new VBox(10, planNameField, createButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        stage.setScene(new Scene(layout, 300, 150));
    }

    public void setPlanCreationListener(PlanCreationListener listener) {
        this.planCreationListener = listener;
    }

    public void showPopup() {
        stage.showAndWait();
    }

    public interface PlanCreationListener {
        void onPlanCreated(String planName);
    }
}
