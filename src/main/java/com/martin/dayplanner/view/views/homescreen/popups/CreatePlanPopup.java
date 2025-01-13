package com.martin.dayplanner.view.views.homescreen.popups;

import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.view.views.homescreen.HomeScreenView;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.Scene;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

public class CreatePlanPopup {

    private final Stage stage;
    private final TextField planNameField;
    private final ComboBox<String> groupSelector;
    private final DatePicker datePicker;
    private final TextField timeInput;
    private PlanCreationListener planCreationListener;

    public CreatePlanPopup(HomeScreenView homeScreenView) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Create New Plan");

        planNameField = new TextField();
        planNameField.setPromptText("Enter plan name");

        // Opprett ComboBox for gruppering
        groupSelector = new ComboBox<>();
        groupSelector.setPromptText("Select group");
        groupSelector.getItems().addAll(
                homeScreenView.getModel().getPlannerGroups().stream()
                        .map(PlannerGroup::getGroupName)
                        .collect(Collectors.toList()));

        // Dato og klokkeslett
        datePicker = new DatePicker();
        datePicker.setPromptText("Select a date");

        timeInput = new TextField();
        timeInput.setPromptText("HH:mm");
        timeInput.setDisable(true); // Deaktivert til dato er valgt

        // Aktiver klokkefelt når en dato er valgt
        datePicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            timeInput.setDisable(newValue == null);
        });

        Button createButton = new Button("Create");
        createButton.setOnAction(e -> {
            String planName = planNameField.getText().trim();
            String selectedGroup = groupSelector.getValue();
            LocalDate selectedDate = datePicker.getValue();
            LocalTime selectedTime = null;

            if (planName.isEmpty()) {
                showAlert("Plan name is required.");
                return;
            }

            if (selectedGroup == null) {
                showAlert("Please select a group.");
                return;
            }

            try {
                if (!timeInput.isDisabled() && !timeInput.getText().isEmpty()) {
                    selectedTime = LocalTime.parse(timeInput.getText());
                }
            } catch (DateTimeParseException ex) {
                showAlert("Invalid time format. Use HH:mm.");
                return;
            }

            if (planCreationListener != null) {
                planCreationListener.onPlanCreated(planName, selectedGroup, selectedDate, selectedTime);
            }
            stage.close();
        });

        VBox layout = new VBox(10, planNameField, groupSelector, datePicker, timeInput, createButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        stage.setScene(new Scene(layout, 400, 300));
    }

    public void setPlanCreationListener(PlanCreationListener listener) {
        this.planCreationListener = listener;
    }

    public void showPopup() {
        stage.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public interface PlanCreationListener {
        void onPlanCreated(String planName, String group, LocalDate date, LocalTime time);
    }
}
