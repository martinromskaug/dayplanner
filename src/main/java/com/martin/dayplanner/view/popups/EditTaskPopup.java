package com.martin.dayplanner.view.popups;

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

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Input fields pre-filled with task data
        Label nameLabel = new Label("Task Name:");
        TextField nameInput = new TextField(task.getName());

        Label dateLabel = new Label("Due Date:");
        DatePicker datePicker = new DatePicker(task.getDueDate());

        Label timeLabel = new Label("Due Time:");
        TextField timeInput = new TextField();
        if (task.getDueTime() != null) {
            timeInput.setText(task.getDueTime().toString());
        }
        timeInput.setPromptText("HH:mm");

        Label priorityLabel = new Label("Priority:");
        ComboBox<TaskPriority> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(TaskPriority.values());
        priorityBox.setValue(task.getPriority() != null ? task.getPriority() : TaskPriority.LOW);

        // Buttons
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        // Button actions
        saveButton.setOnAction(e -> {
            String newName = nameInput.getText();
            LocalDate newDueDate = datePicker.getValue();
            LocalTime newDueTime = null;
            if (!timeInput.getText().isEmpty()) {
                newDueTime = LocalTime.parse(timeInput.getText());
            }
            TaskPriority newPriority = priorityBox.getValue();

            if (listener != null && !newName.isEmpty()) {
                listener.onTaskEdited(task, newName, newDueDate, newDueTime, newPriority);
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
        grid.add(saveButton, 0, 4);
        grid.add(cancelButton, 1, 4);

        // Set up Scene and Stage
        Scene scene = new Scene(grid, 400, 300);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }
}
