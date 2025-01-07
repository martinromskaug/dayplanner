package com.martin.dayplanner.view.views;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseView {

    protected Button createButton(String text) {
        return new Button(text);
    }

    protected ListView<String> createListView() {
        return new ListView<>();
    }

    protected Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    protected VBox wrapInWhiteBox(Region content, String styleClass) {
        VBox box = new VBox(content);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.getStyleClass().add(styleClass);
        return box;
    }

    protected VBox createDateTimeBox() {
        Label dateLabel = createLabel("", "date-label");
        Label yearLabel = createLabel("", "year-label");
        Label clockLabel = createLabel("", "clock-label");

        updateDateTime(dateLabel, yearLabel, clockLabel);
        setupDynamicClock(dateLabel, yearLabel, clockLabel);

        VBox dateYearBox = new VBox(5, dateLabel, yearLabel);
        dateYearBox.setAlignment(Pos.CENTER);

        HBox dateTimeBox = new HBox(20, dateYearBox, clockLabel);
        dateTimeBox.setAlignment(Pos.CENTER_RIGHT);
        VBox box = new VBox(dateTimeBox);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    protected VBox createTopSection(String title) {
        Label titleLabel = createLabel(title, "title-label");
        VBox dateTimeBox = createDateTimeBox();
        HBox titleBox = new HBox(titleLabel, dateTimeBox);

        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 20, 10, 20));
        titleBox.getStyleClass().add("title-box");

        return wrapInWhiteBox(titleBox, "title-box-wrapper");
    }

    protected VBox createSection(String title, ListView<String> listView, String style) {
        Label sectionLabel = createLabel(title, "section-label");
        VBox sectionBox = wrapInWhiteBox(listView, style);
        return new VBox(5, sectionLabel, sectionBox);
    }

    protected VBox createSection(String title, ListView<String> listView, HBox buttonRow) {
        Label sectionLabel = createLabel(title, "section-label");
        VBox sectionBox = wrapInWhiteBox(listView, "plans-box");
        return new VBox(5, sectionLabel, sectionBox, buttonRow);
    }

    private void setupDynamicClock(Label dateLabel, Label yearLabel, Label clockLabel) {
        Timeline clockTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateDateTime(dateLabel, yearLabel, clockLabel)));
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
