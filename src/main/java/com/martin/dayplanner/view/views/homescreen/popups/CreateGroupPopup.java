package com.martin.dayplanner.view.views.homescreen.popups;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.Scene;

public class CreateGroupPopup {

    private final Stage stage;
    private final TextField groupNameField;
    private GroupCreationListener groupCreationListener;

    public CreateGroupPopup() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Create New Group");

        groupNameField = new TextField();
        groupNameField.setPromptText("Enter group name");

        Button createButton = new Button("Create");
        createButton.setOnAction(e -> {
            String groupName = groupNameField.getText().trim();
            if (groupName.isEmpty()) {
                showAlert("Group name cannot be empty.");
                return;
            }

            if (groupCreationListener != null) {
                groupCreationListener.onGroupCreated(groupName);
            }
            stage.close();
        });

        VBox layout = new VBox(10, groupNameField, createButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        stage.setScene(new Scene(layout, 300, 150));
    }

    public void setGroupCreationListener(GroupCreationListener listener) {
        this.groupCreationListener = listener;
    }

    public void showPopup() {
        stage.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public interface GroupCreationListener {
        void onGroupCreated(String groupName);
    }
}
