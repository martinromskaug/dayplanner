package com.martin.dayplanner.view.views.homescreen;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.view.Viewable;
import com.martin.dayplanner.view.views.BaseView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class HomeScreenView extends BaseView implements Viewable {

    private final ViewableHomeScreen model;
    private final Button addGroupButton;
    private final Button removePlanButton;
    private final Button editPlanButton;
    private final Button createNewPlanButton;
    private final TreeView<String> plansTreeView;
    private final BorderPane root;

    public HomeScreenView(ViewableHomeScreen model) {
        this.model = model;

        addGroupButton = createButton("Add Plan");
        removePlanButton = createButton("Remove Plan");
        editPlanButton = createButton("Edit Plan");
        createNewPlanButton = createButton("Add Plan");

        plansTreeView = createTreeView();

        root = new BorderPane();
        updateHomeScreen();
        setupLayout();
    }

    public void updateHomeScreen() {
        updatePlannerList();
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

        HBox plansButtonRow = createButtonRow();
        centerGrid.add(createSection("Your Plans", plansTreeView, plansButtonRow), 0, 0, 1, 2);

        return centerGrid;
    }

    private HBox createButtonRow() {
        HBox buttonRow = new HBox(10, removePlanButton, editPlanButton, createNewPlanButton, addGroupButton);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getStyleClass().add("button-row");
        return buttonRow;
    }

    private TreeView<String> createTreeView() {
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);
        return new TreeView<>(root);
    }

    private void updatePlannerList() {
        TreeItem<String> root = new TreeItem<>("Your Plans");
        root.setExpanded(true);

        List<PlannerGroup> plannerGroups = model.getPlannerGroups();
        for (PlannerGroup group : plannerGroups) {
            TreeItem<String> groupItem = new TreeItem<>(group.getGroupName());

            List<Planner> planners = model.getPlannersForGroup(group);
            for (Planner planner : planners) {
                TreeItem<String> plannerItem = new TreeItem<>(planner.getPlannerName());
                groupItem.getChildren().add(plannerItem);
            }

            root.getChildren().add(groupItem);
        }

        plansTreeView.setRoot(root);
    }

    @Override
    public BorderPane getLayout() {
        return root;
    }

    public Button getCreateNewPlanButton() {
        return createNewPlanButton;
    }

    public Button getEditPlanButton() {
        return editPlanButton;
    }

    public Button getRemovePlanButton() {
        return removePlanButton;
    }

    public Button getAddGroupButton() {
        return addGroupButton;
    }

    public TreeView<String> getPlansTreeView() {
        return plansTreeView;
    }
}
