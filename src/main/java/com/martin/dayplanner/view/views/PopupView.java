package com.martin.dayplanner.view.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.model.task.TaskPriority;

public class PopupView {

    private final Stage stage;
    private final GridPane layout;
    private final Map<PopupFieldKey, Object> metadata = new HashMap<>();
    private final Map<PopupFieldKey, Control> fields = new HashMap<>();
    private final Label errorLabel;

    public PopupView(String title) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        layout.add(errorLabel, 0, 10, 2, 1);
    }

    public void setMetadata(PopupFieldKey key, Object value) {
        metadata.put(key, value);
    }

    public void addNameField(String label, String placeholder, String previousValue) {
        Label fieldLabel = new Label(label);
        TextField textField = new TextField();
        textField.setPromptText(placeholder);

        // Sett tidligere verdi som standard hvis den finnes
        if (previousValue != null) {
            textField.setText(previousValue);
        }

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (textField.getText().trim().isEmpty()) {
                    errorLabel.setText("Name is required.");
                } else {
                    errorLabel.setText("");
                }
            }
        });

        fields.put(PopupFieldKey.NAME, textField);
        int row = fields.size() - 1;
        layout.add(fieldLabel, 0, row);
        layout.add(textField, 1, row);
    }

    public void addParentGroupComboBox(String label, String placeholder, PlannerGroup previousValue,
            List<PlannerGroup> options) {
        Label fieldLabel = new Label(label);
        ComboBox<PlannerGroup> comboBox = new ComboBox<>();
        comboBox.setPromptText(placeholder);

        // Legg til alle PlannerGroups
        comboBox.getItems().addAll(options);

        // Sett tidligere verdi som standard hvis den finnes
        if (previousValue != null) {
            comboBox.setValue(previousValue);
        }

        // Validering for Parent Group
        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && comboBox.getValue() == null) {
                errorLabel.setText("Parent Group is required.");
            } else {
                errorLabel.setText("");
            }
        });

        fields.put(PopupFieldKey.PARENT_GROUP, comboBox);
        int row = fields.size() - 1;
        layout.add(fieldLabel, 0, row);
        layout.add(comboBox, 1, row);
    }

    public void addPriorityComboBox(String label, String placeholder, TaskPriority previousValue) {
        Label fieldLabel = new Label(label);
        ComboBox<TaskPriority> comboBox = new ComboBox<>();
        comboBox.setPromptText(placeholder);

        // Legg til TaskPriority enum-verdier
        comboBox.getItems().addAll(TaskPriority.values());

        // Sett tidligere verdi som standard hvis den finnes
        if (previousValue != null) {
            comboBox.setValue(previousValue);
        }

        fields.put(PopupFieldKey.PRIORITY, comboBox);
        int row = fields.size() - 1;
        layout.add(fieldLabel, 0, row);
        layout.add(comboBox, 1, row);
    }

    public void addDateTimePicker(String dateLabel, String timeLabel, LocalDate previousDate, LocalTime previousTime) {
        Label dateFieldLabel = new Label(dateLabel);
        DatePicker datePicker = new DatePicker();
        fields.put(PopupFieldKey.DUE_DATE, datePicker);

        // Sett tidligere dato hvis tilgjengelig
        if (previousDate != null) {
            datePicker.setValue(previousDate);
        }

        Label timeFieldLabel = new Label(timeLabel);
        TextField timeField = new TextField();
        timeField.setPromptText("HH:mm");
        fields.put(PopupFieldKey.DUE_TIME, timeField);

        // Sett tidligere tid hvis tilgjengelig
        if (previousTime != null) {
            timeField.setText(previousTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        // Layout-justering
        int row = fields.size() / 2; // Antall rader basert på antall felter
        layout.add(dateFieldLabel, 0, row);
        layout.add(datePicker, 1, row);
        layout.add(timeFieldLabel, 0, row + 1);
        layout.add(timeField, 1, row + 1);
    }

    public void addLabel(String text) {
        Label label = new Label(text);
        layout.add(label, 0, layout.getChildren().size());
    }

    public void setActions(Consumer<Map<PopupFieldKey, Object>> onConfirm, Runnable onCancel) {
        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        confirmButton.setOnAction(e -> {
            if (validateFields()) {
                Map<PopupFieldKey, Object> values = getFieldValues();
                onConfirm.accept(values);
                stage.close();
            } else {
                errorLabel.setText("Please fill in all required fields.");
            }
        });

        cancelButton.setOnAction(e -> {
            if (onCancel != null) {
                onCancel.run();
            }
            stage.close();
        });

        int row = fields.size();
        layout.add(confirmButton, 0, row);
        layout.add(cancelButton, 1, row);
    }

    public void show() {
        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private Map<PopupFieldKey, Object> getFieldValues() {
        Map<PopupFieldKey, Object> values = new HashMap<>(metadata);
        for (Map.Entry<PopupFieldKey, Control> entry : fields.entrySet()) {
            PopupFieldKey key = entry.getKey();
            Control control = entry.getValue();

            if (control instanceof TextField) {
                values.put(key, ((TextField) control).getText().trim());
            } else if (control instanceof ComboBox) {
                if (key == PopupFieldKey.PARENT_GROUP) {
                    // Hent ID fra PlannerGroup-objektet for PARENT_GROUP
                    PlannerGroup selectedGroup = (PlannerGroup) ((ComboBox<?>) control).getValue();
                    values.put(key, selectedGroup != null ? selectedGroup.getId() : null);
                } else if (key == PopupFieldKey.PRIORITY) {
                    // Hent TaskPriority direkte
                    values.put(key, ((ComboBox<?>) control).getValue());
                }
            } else if (control instanceof DatePicker) {
                DatePicker datePicker = (DatePicker) control;
                values.put(key, datePicker.getValue());
            }
        }
        return values;
    }

    private boolean validateFields() {
        TextField nameField = (TextField) fields.get(PopupFieldKey.NAME);
        if (nameField != null && nameField.getText().trim().isEmpty()) {
            errorLabel.setText("Name is required.");
            return false;
        }

        ComboBox<?> parentGroupField = (ComboBox<?>) fields.get(PopupFieldKey.PARENT_GROUP);
        if (parentGroupField != null && parentGroupField.getValue() == null) {
            errorLabel.setText("Parent Group is required.");
            return false;
        }

        return true;
    }
}
