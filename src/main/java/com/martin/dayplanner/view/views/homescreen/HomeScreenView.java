package com.martin.dayplanner.view.views.homescreen;

import com.martin.dayplanner.view.views.BaseView;
import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.view.Viewable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class HomeScreenView extends BaseView implements Viewable {

    private final ViewableHomeScreen model;
    private final Button removePlanButton;
    private final Button createNewPlanButton;
    private final Button goToPlanButton;
    private final ListView<String> plansListView;
    private final ListView<String> deadlinesListView;
    private final ListView<String> activeTasksListView;
    private final BorderPane root;

    public HomeScreenView(ViewableHomeScreen model) {
        this.model = model;

        removePlanButton = createButton("Remove Plan");
        createNewPlanButton = createButton("Add Plan");
        goToPlanButton = createButton("Go to Plan");

        plansListView = createListView();
        deadlinesListView = createListView();
        activeTasksListView = createListView();

        root = new BorderPane();
        updatePlannerList();
        updateActiveTaskList();
        setupLayout();
    }

    private void setupLayout() {
        root.setTop(createTopSection("Day Planner"));
        root.setCenter(createCenterSection());
    }

    private GridPane createCenterSection() {
        GridPane centerGrid = new GridPane();
        centerGrid.setPadding(new Insets(20, 20, 30, 20));
        centerGrid.setHgap(15);
        centerGrid.setVgap(15);
        centerGrid.getStyleClass().add("center-grid");

        // Your Plans-seksjon med knappene under listen
        HBox plansButtonRow = createButtonRow();
        centerGrid.add(createSection("Your Plans", plansListView, plansButtonRow), 0, 0, 1, 2);

        // Deadlines og Active Tasks seksjoner
        centerGrid.add(createSection("Deadlines", deadlinesListView, "tasks-box"), 1, 1);
        centerGrid.add(createSection("Active Tasks", activeTasksListView, "tasks-box"), 1, 0);

        return centerGrid;
    }

    private HBox createButtonRow() {
        HBox buttonRow = new HBox(10, removePlanButton, createNewPlanButton, goToPlanButton);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getStyleClass().add("button-row");
        return buttonRow;
    }

    public void updatePlannerList() {
        plansListView.getItems().setAll(
                model.getPlanners().stream()
                        .map(Planner::getPlannerName)
                        .toList());
    }

    public void updateActiveTaskList() {
        activeTasksListView.getItems().setAll(
                model.getActiveTasks().stream()
                        .map(task -> task.getPlannerName() + ": " + task.getTaskName())
                        .toList());
    }

    @Override
    public BorderPane getLayout() {
        return root;
    }

    public Button getCreateNewPlanButton() {
        return createNewPlanButton;
    }

    public Button getGoToPlanButton() {
        return goToPlanButton;
    }

    public Button getRemovePlanButton() {
        return removePlanButton;
    }

    public ListView<String> getPlansListView() {
        return plansListView;
    }

    public ListView<String> getDeadlinesListView() {
        return deadlinesListView;
    }

    public ListView<String> getActiveTasksListView() {
        return activeTasksListView;
    }
}