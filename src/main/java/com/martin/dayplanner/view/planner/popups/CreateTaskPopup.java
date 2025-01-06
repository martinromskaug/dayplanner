package com.martin.dayplanner.view.planner.popups;

import com.martin.dayplanner.model.task.TaskPriority;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class CreateTaskPopup {

    public interface TaskCreationListener {
        void onTaskCreated(String name, LocalDate dueDate, LocalTime dueTime, TaskPriority priority);
    }

    private TaskCreationListener listener;

    public void setTaskCreationListener(TaskCreationListener listener) {
        this.listener = listener;
    }

    public void showPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Add New Task");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Input fields
        Label nameLabel = new Label("Task Name:");
        TextField nameInput = new TextField();

        Label dateLabel = new Label("Due Date:");
        DatePicker datePicker = new DatePicker();

        Label timeLabel = new Label("Due Time:");
        TextField timeInput = new TextField();
        timeInput.setPromptText("HH:mm");

        Label priorityLabel = new Label("Priority:");
        ComboBox<TaskPriority> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(TaskPriority.values());

        // Buttons
        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Button actions
        addButton.setOnAction(e -> {
            String name = nameInput.getText().trim();

            // Validate name
            if (name.isEmpty()) {
                errorLabel.setText("Task name is required.");
                return;
            }

            LocalDate dueDate = datePicker.getValue();
            LocalTime dueTime = null;
            TaskPriority priority = priorityBox.getValue();

            try {
                if (!timeInput.getText().isEmpty()) {
                    dueTime = LocalTime.parse(timeInput.getText());
                }
            } catch (DateTimeParseException ex) {
                errorLabel.setText("Invalid time format. Use HH:mm.");
                return;
            }

            if (listener != null) {
                listener.onTaskCreated(name, dueDate, dueTime, priority);
            }
            popupStage.close();
        });

        cancelButton.setOnAction(e -> popupStage.close());

        // Add elements to grid
        grid.add(nameLabel, 0, 0);
        grid.add(nameInput, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(timeLabel, 0, 2);
        grid.add(timeInput, 1, 2);
        grid.add(priorityLabel, 0, 3);
        grid.add(priorityBox, 1, 3);
        grid.add(addButton, 0, 4);
        grid.add(cancelButton, 1, 4);
        grid.add(errorLabel, 0, 5, 2, 1);

        // Set up Scene and Stage
        Scene scene = new Scene(grid, 400, 300);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }
}
