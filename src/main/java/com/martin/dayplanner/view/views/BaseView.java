package com.martin.dayplanner.view.views;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseView {

    protected Button createButton(String text) {
        return new Button(text);
    }

    protected Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        label.setWrapText(true);
        return label;
    }

    protected VBox wrapInWhiteBox(Region content, String styleClass) {
        VBox box = new VBox(content);
        box.getStyleClass().add(styleClass);
        return box;
    }

    protected VBox createDateTimeBox() {
        Label dateLabel = createLabel("", "date-label");
        Label yearLabel = createLabel("", "year-label");
        Label clockLabel = createLabel("", "clock-label");

        // Sett fast bredde for klokken
        clockLabel.setPrefWidth(100); // Juster denne verdien til ønsket bredde
        clockLabel.setAlignment(Pos.CENTER_RIGHT); // Sentraliser teksten i boksen

        updateDateTime(dateLabel, yearLabel, clockLabel);
        setupDynamicClock(dateLabel, yearLabel, clockLabel);

        VBox dateYearBox = new VBox(5, dateLabel, yearLabel);
        dateYearBox.setAlignment(Pos.CENTER);

        HBox dateTimeBox = new HBox(20, dateYearBox, clockLabel);
        dateTimeBox.setAlignment(Pos.CENTER_RIGHT);

        VBox box = new VBox(dateTimeBox);
        return box;
    }

    protected VBox createTopSection(String title) {
        Label titleLabel = createLabel(title, "title-label");
        // titleLabel.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        VBox dateTimeBox = createDateTimeBox();
        dateTimeBox.getStyleClass().add("date-time-box");
        // dateTimeBox.setStyle("-fx-border-color: blue; -fx-border-width: 2;");

        HBox titleBox = new HBox();
        titleBox.getChildren().addAll(titleLabel, dateTimeBox);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        HBox.setHgrow(dateTimeBox, Priority.NEVER);

        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        dateTimeBox.setAlignment(Pos.CENTER_RIGHT);

        titleBox.getStyleClass().add("title-box");
        // titleBox.setStyle("-fx-border-color: green; -fx-border-width: 2;");

        return wrapInWhiteBox(titleBox, "title-box-wrapper");
    }

    protected VBox createSection(String title, ListView<ListItemData> listView, String style) {
        Label sectionLabel = createLabel(title, "section-label");
        VBox sectionBox = wrapInWhiteBox(listView, style);
        return new VBox(5, sectionLabel, sectionBox);
    }

    protected VBox createSection(String title, ListView<ListItemData> listView, HBox buttonRow) {
        Label sectionLabel = createLabel(title, "section-label");
        VBox sectionBox = wrapInWhiteBox(listView, "plans-box");
        return new VBox(5, sectionLabel, sectionBox, buttonRow);
    }

    protected VBox createSection(String title, TreeView<ListItemData> treeView, String style) {
        Label sectionLabel = createLabel(title, "section-label");
        VBox sectionBox = wrapInWhiteBox(treeView, style);
        return new VBox(5, sectionLabel, sectionBox);
    }

    protected VBox createSection(String title, TreeView<ListItemData> treeView, HBox buttonRow) {
        Label sectionLabel = createLabel(title, "section-label");
        VBox sectionBox = wrapInWhiteBox(treeView, "plans-box");
        return new VBox(5, sectionLabel, sectionBox, buttonRow);
    }

    protected VBox createSection(String title, TreeView<ListItemData> treeView, HBox buttonRow1, HBox buttonRow2) {
        Label sectionLabel = createLabel(title, "section-label");
        VBox sectionBox = wrapInWhiteBox(treeView, "plans-box");
        return new VBox(5, sectionLabel, sectionBox, buttonRow1, buttonRow2);
    }

    private void setupDynamicClock(Label dateLabel, Label yearLabel, Label clockLabel) {
        Timeline clockTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), _ -> updateDateTime(dateLabel, yearLabel, clockLabel)));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
    }

    private void updateDateTime(Label dateLabel, Label yearLabel, Label clockLabel) {
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText(now.format(DateTimeFormatter.ofPattern("MMM dd")));
        yearLabel.setText(now.format(DateTimeFormatter.ofPattern("yyyy")));
        clockLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
