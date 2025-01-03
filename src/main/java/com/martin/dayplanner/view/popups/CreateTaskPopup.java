package com.martin.dayplanner.view.popups;

import com.martin.dayplanner.model.task.TaskPriority;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CreateTaskPopup {

    private TextField taskNameField;
    private ComboBox<TaskPriority> priorityBox;
    private DatePicker dueDatePicker;
    private Button saveButton;
    private Stage popupStage;

    public CreateTaskPopup(Stage ownerStage) {
        popupStage = new Stage();
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(ownerStage);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setVgap(10);
        layout.setHgap(10);

        taskNameField = new TextField();
        taskNameField.setPromptText("Task Name");

        priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(TaskPriority.values()); // Bruker TaskPriority enum
        priorityBox.setPromptText("Select Priority");

        dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due Date");

        saveButton = new Button("Save");

        layout.add(new Label("Task Name:"), 0, 0);
        layout.add(taskNameField, 1, 0);
        layout.add(new Label("Priority:"), 0, 1);
        layout.add(priorityBox, 1, 1);
        layout.add(new Label("Due Date:"), 0, 2);
        layout.add(dueDatePicker, 1, 2);
        layout.add(saveButton, 1, 3);

        Scene scene = new Scene(layout);
        popupStage.setScene(scene);
        popupStage.setTitle("Create New Task");
    }

    public void show() {
        popupStage.show();
    }

    public void close() {
        popupStage.close();
    }

    public TextField getTaskNameField() {
        return taskNameField;
    }

    public ComboBox<TaskPriority> getPriorityBox() {
        return priorityBox;
    }

    public DatePicker getDueDatePicker() {
        return dueDatePicker;
    }

    public Button getSaveButton() {
        return saveButton;
    }
}
