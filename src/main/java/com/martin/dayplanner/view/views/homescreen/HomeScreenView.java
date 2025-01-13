package com.martin.dayplanner.view.views.homescreen;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.model.task.Task;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeScreenView extends BaseView implements Viewable {

    private final ViewableHomeScreen model;
    private final Button addGroupButton;
    private final Button removePlanButton;
    private final Button editPlanButton;
    private final Button createNewPlanButton;
    private TreeView<String> plansTreeView;
    private TreeView<String> deadlinesTreeView;
    private TreeView<String> activeTasksTreeView;
    private final BorderPane root;

    public HomeScreenView(ViewableHomeScreen model) {
        this.model = model;

        addGroupButton = createButton("Add Group");
        removePlanButton = createButton("Remove Plan");
        editPlanButton = createButton("Edit Plan");
        createNewPlanButton = createButton("Add Plan");

        plansTreeView = createTreeView();
        deadlinesTreeView = createTreeView();
        activeTasksTreeView = createTreeView();

        root = new BorderPane();
        updateHomeScreen();
        setupLayout();
    }

    public void updateHomeScreen() {
        updatePlannerList();
        updateDeadlinesList();
        updateActiveTasksList();
    }

    private void setupTreeViewCellFactory(TreeView<String> treeView) {
        treeView.setCellFactory(tv -> new PlanTreeCell());
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

        centerGrid.add(createSection("Deadlines", deadlinesTreeView, "tasks-box"), 1, 1);
        centerGrid.add(createSection("Active Tasks", activeTasksTreeView, "tasks-box"), 1, 0);

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

        TreeView<String> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);

        setupTreeViewCellFactory(treeView);

        return treeView;
    }

    private void updatePlannerList() {
        TreeItem<String> root = new TreeItem<>("Root");
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

    private void updateDeadlinesList() {
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);

        // Hent planene som har deadlines
        List<Planner> plannersWithDeadlines = model.getPlannersWithDeadline();

        // Gruppér planene etter dato
        Map<LocalDate, List<Planner>> plannersByDate = plannersWithDeadlines.stream()
                .collect(Collectors.groupingBy(Planner::getDueDate));

        // Formattere dato og tid
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Iterer gjennom datoene og legg til planene som barn
        for (Map.Entry<LocalDate, List<Planner>> entry : plannersByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<Planner> planners = entry.getValue();

            // Opprett en TreeItem for datoen
            TreeItem<String> dateItem = new TreeItem<>(date.format(dateFormatter));
            dateItem.setExpanded(true);

            // Legg til alle planene under datoen
            for (Planner planner : planners) {
                String formattedPlanner = String.format("%s %s",
                        planner.getDueTime().format(timeFormatter), // Formater klokkeslett
                        planner.getPlannerName()); // Legg til navn på planner
                TreeItem<String> plannerItem = new TreeItem<>(formattedPlanner);
                dateItem.getChildren().add(plannerItem);
            }

            // Legg til datoen som en child av root
            root.getChildren().add(dateItem);
        }

        deadlinesTreeView.setRoot(root);
    }

    private void updateActiveTasksList() {
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);

        // Hent gruppert map over aktive oppgaver
        Map<String, List<Task>> tasksByPlanner = model.getActiveTasks();

        // Bygg trestrukturen
        for (Map.Entry<String, List<Task>> entry : tasksByPlanner.entrySet()) {
            String plannerName = entry.getKey();
            List<Task> tasks = entry.getValue();

            TreeItem<String> plannerItem = new TreeItem<>(plannerName);
            plannerItem.setExpanded(true);

            for (Task task : tasks) {
                TreeItem<String> taskItem = new TreeItem<>(task.getTaskName());
                plannerItem.getChildren().add(taskItem);
            }

            root.getChildren().add(plannerItem);
        }

        activeTasksTreeView.setRoot(root);
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

    public TreeView<String> getDeadlinesTreeView() {
        return deadlinesTreeView;
    }

    public TreeView<String> getActiveTasksTreeView() {
        return activeTasksTreeView;
    }

    public ViewableHomeScreen getModel() {
        return model;
    }
}
