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
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Consumer;

import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.model.task.TaskPriority;

public class PopupView {

    private final Stage stage;
    private final GridPane layout;
    private final Map<PopupFieldKey, Object> metadata = new HashMap<>();
    private final Map<PopupFieldKey, Control> fields = new HashMap<>();
    private final Label errorLabel;

    private final ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

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

        if (previousValue != null) {
            textField.setText(previousValue);
        }

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && textField.getText().trim().isEmpty()) {
                showErrors(Collections.singletonList(bundle.getString("error.name_required")));
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
        comboBox.getItems().addAll(options);

        if (previousValue != null) {
            comboBox.setValue(previousValue);
        }

        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && comboBox.getValue() == null) {
                showErrors(Collections.singletonList(bundle.getString("error.parent_group_required")));
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
        comboBox.getItems().addAll(TaskPriority.values());

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

        if (previousDate != null) {
            datePicker.setValue(previousDate);
        }

        Label timeFieldLabel = new Label(timeLabel);
        TextField timeField = new TextField();
        timeField.setPromptText("HH:mm");
        fields.put(PopupFieldKey.DUE_TIME, timeField);

        if (previousTime != null) {
            timeField.setText(previousTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        int row = fields.size() / 2;
        layout.add(dateFieldLabel, 0, row);
        layout.add(datePicker, 1, row);
        layout.add(timeFieldLabel, 0, row + 1);
        layout.add(timeField, 1, row + 1);
    }

    public void addLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        int row = layout.getChildren().size();
        layout.add(label, 0, row, 2, 1);
    }

    public void setActions(Consumer<Map<PopupFieldKey, Object>> onConfirm, Runnable onCancel) {
        Button confirmButton = new Button(bundle.getString("button.confirm"));
        Button cancelButton = new Button(bundle.getString("button.cancel"));

        confirmButton.setOnAction(e -> {
            if (validateFields()) {
                Map<PopupFieldKey, Object> values = getFieldValues();
                onConfirm.accept(values);
                stage.close();
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
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private Map<PopupFieldKey, Object> getFieldValues() {
        Map<PopupFieldKey, Object> values = new HashMap<>(metadata);
        for (Map.Entry<PopupFieldKey, Control> entry : fields.entrySet()) {
            PopupFieldKey key = entry.getKey();
            Control control = entry.getValue();

            if (control instanceof TextField) {
                String textValue = ((TextField) control).getText().trim();
                if (key == PopupFieldKey.DUE_TIME && !textValue.isEmpty()) {
                    try {
                        LocalTime time = LocalTime.parse(textValue, DateTimeFormatter.ofPattern("HH:mm"));
                        values.put(key, time);
                    } catch (DateTimeParseException e) {
                        showErrors(Collections.singletonList(bundle.getString("error.invalid_time_format")));
                    }
                } else {
                    values.put(key, textValue.isEmpty() ? null : textValue);
                }
            } else if (control instanceof ComboBox) {
                if (key == PopupFieldKey.PARENT_GROUP) {
                    PlannerGroup group = (PlannerGroup) ((ComboBox<?>) control).getValue();
                    values.put(key, group != null ? group.getId() : null);
                } else {
                    values.put(key, ((ComboBox<?>) control).getValue());
                }
            } else if (control instanceof DatePicker) {
                values.put(key, ((DatePicker) control).getValue());
            }
        }
        return values;
    }

    private boolean validateFields() {
        List<String> errors = new ArrayList<>();

        TextField nameField = (TextField) fields.get(PopupFieldKey.NAME);
        if (nameField != null && nameField.getText().trim().isEmpty()) {
            errors.add(bundle.getString("error.name_required"));
        }

        ComboBox<?> parentGroupField = (ComboBox<?>) fields.get(PopupFieldKey.PARENT_GROUP);
        if (parentGroupField != null && parentGroupField.getValue() == null) {
            errors.add(bundle.getString("error.parent_group_required"));
        }

        if (!errors.isEmpty()) {
            showErrors(errors);
            return false;
        }
        return true;
    }

    private void showErrors(List<String> errors) {
        StringBuilder errorText = new StringBuilder();
        for (String error : errors) {
            errorText.append("- ").append(error).append("\n");
        }
        errorLabel.setText(errorText.toString());
    }
}
