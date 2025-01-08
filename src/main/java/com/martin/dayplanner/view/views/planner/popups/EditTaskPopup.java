package com.martin.dayplanner.view.views.planner.popups;

import com.martin.dayplanner.model.task.Task;
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

public class EditTaskPopup {

    public interface TaskEditListener {
        void onTaskEdited(Task task, String newName, LocalDate newDueDate, LocalTime newDueTime,
                TaskPriority newPriority);
    }

    private TaskEditListener listener;

    public void setTaskEditListener(TaskEditListener listener) {
        this.listener = listener;
    }

    public void showPopup(Task task) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Edit Task");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label nameLabel = new Label("Task Name:");
        TextField nameInput = new TextField(task.getTaskName());

        Label dateLabel = new Label("Due Date (optional):");
        DatePicker datePicker = new DatePicker(task.getDueDate());

        Label timeLabel = new Label("Due Time (optional):");
        TextField timeInput = new TextField();
        if (task.getDueTime() != null) {
            timeInput.setText(task.getDueTime().toString());
        }
        timeInput.setPromptText("HH:mm");

        Label priorityLabel = new Label("Priority (optional):");
        ComboBox<TaskPriority> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(TaskPriority.values());
        priorityBox.setValue(task.getPriority() != null ? task.getPriority() : TaskPriority.LOW);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        saveButton.setOnAction(e -> {
            String newName = nameInput.getText().trim();

            if (newName.isEmpty()) {
                errorLabel.setText("Task name is required.");
                return;
            }

            LocalDate newDueDate = datePicker.getValue();
            LocalTime newDueTime = null;
            TaskPriority newPriority = priorityBox.getValue();

            try {
                if (!timeInput.getText().isEmpty()) {
                    newDueTime = LocalTime.parse(timeInput.getText());
                }
            } catch (DateTimeParseException ex) {
                errorLabel.setText("Invalid time format. Use HH:mm.");
                return;
            }

            if (listener != null) {
                listener.onTaskEdited(task, newName, newDueDate, newDueTime, newPriority);
            }
            popupStage.close();
        });

        cancelButton.setOnAction(e -> popupStage.close());

        grid.add(nameLabel, 0, 0);
        grid.add(nameInput, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(timeLabel, 0, 2);
        grid.add(timeInput, 1, 2);
        grid.add(priorityLabel, 0, 3);
        grid.add(priorityBox, 1, 3);
        grid.add(saveButton, 0, 4);
        grid.add(cancelButton, 1, 4);
        grid.add(errorLabel, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 400, 300);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }
}
