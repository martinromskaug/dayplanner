package com.martin.dayplanner.view.views.homescreen;

import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.model.PlannerGroup;
import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.view.views.BaseView;
import com.martin.dayplanner.view.views.ListItemData;
import com.martin.dayplanner.view.views.Specimen;
import com.martin.dayplanner.view.views.Viewable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeScreenView extends BaseView implements Viewable {

    private final ViewableHomeScreen model;
    private final Button removeButton;
    private final Button editButton;
    private final Button addButton;
    private TreeView<ListItemData> plansTreeView;
    private TreeView<ListItemData> deadlinesTreeView;
    private TreeView<ListItemData> activeTasksTreeView;
    private final HomeScreenPopupConfigurator popup;
    private final BorderPane root;

    public HomeScreenView(ViewableHomeScreen model) {
        this.model = model;

        removeButton = createButton("Remove");
        editButton = createButton("Edit");
        addButton = createButton("Add");

        plansTreeView = createTreeView("tree-view-focusable");
        deadlinesTreeView = createTreeView("tree-view-non-focusable");
        activeTasksTreeView = createTreeView("tree-view-non-focusable");

        this.popup = new HomeScreenPopupConfigurator(model);

        root = new BorderPane();
        updateHomeScreen();
        setupLayout();
    }

    public void updateHomeScreen() {
        updatePlannerList();
        updateDeadlinesList();
        updateActiveTasksList();
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

        HBox buttonRow = createButtonRow(removeButton, editButton, addButton);
        buttonRow.getStyleClass().add("button-row");
        centerGrid.add(createSection("Your Plans", plansTreeView, buttonRow), 0, 0, 1, 2);

        centerGrid.add(createSection("Deadlines", deadlinesTreeView, "tasks-box"), 1, 0);
        centerGrid.add(createSection("Active Tasks", activeTasksTreeView, "tasks-box"), 1, 1);

        return centerGrid;
    }

    private HBox createButtonRow(Button button1, Button button2, Button button3) {
        HBox buttonRow = new HBox(10, button1, button2, button3);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getStyleClass().add("button-row");
        return buttonRow;
    }

    private TreeView<ListItemData> createTreeView(String styleClass) {
        TreeItem<ListItemData> root = new TreeItem<>(new ListItemData("root", "Root", Specimen.ROOT));
        root.setExpanded(true);

        TreeView<ListItemData> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.getStyleClass().add(styleClass);

        return treeView;
    }

    private void updatePlannerList() {
        TreeItem<ListItemData> root = new TreeItem<>(new ListItemData("root", "Root", Specimen.ROOT));
        root.setExpanded(true);

        List<PlannerGroup> plannerGroups = model.getPlannerGroups();
        for (PlannerGroup group : plannerGroups) {
            TreeItem<ListItemData> groupItem = new TreeItem<>(
                    new ListItemData(group.getId(), group.getGroupName(), Specimen.GROUP));

            List<Planner> planners = model.getPlannersForGroup(group.getId());
            for (Planner planner : planners) {
                TreeItem<ListItemData> plannerItem = new TreeItem<>(
                        new ListItemData(planner.getId(), planner.getPlannerName(), Specimen.PLANNER));
                groupItem.getChildren().add(plannerItem);
            }

            root.getChildren().add(groupItem);
        }

        plansTreeView.setRoot(root);
    }

    private void updateDeadlinesList() {
        TreeItem<ListItemData> root = new TreeItem<>(new ListItemData("root", "Root", Specimen.ROOT));
        root.setExpanded(true);

        List<Planner> plannersWithDeadlines = model.getPlannersWithDeadline();

        // Sorter planleggere etter dato og tid
        plannersWithDeadlines.sort(Comparator
                .comparing(Planner::getDueDate)
                .thenComparing(Planner::getDueTime));

        // Behold sorteringsrekkefølge ved å bruke LinkedHashMap
        Map<LocalDate, List<Planner>> plannersByDate = plannersWithDeadlines.stream()
                .collect(Collectors.groupingBy(
                        Planner::getDueDate,
                        LinkedHashMap::new,
                        Collectors.toList()));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MMM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Map.Entry<LocalDate, List<Planner>> entry : plannersByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<Planner> planners = entry.getValue();

            String dueDateAndDays = String.format("%s             %s", date.format(dateFormatter), daysUntil(date));

            TreeItem<ListItemData> dateItem = new TreeItem<>(
                    new ListItemData(date.toString(), dueDateAndDays, Specimen.OTHER));
            dateItem.setExpanded(true);

            for (Planner planner : planners) {
                String formattedPlanner = String.format("%s %s -%s",
                        planner.getDueTime() != null ? planner.getDueTime().format(timeFormatter) : "N/A",
                        planner.getPlannerName() != null ? planner.getPlannerName() : "No Name",
                        model.getParentGroupByPlannerId(planner.getId()) != null
                                ? model.getParentGroupByPlannerId(planner.getId())
                                : "No Group");

                // Opprett TreeItem for planleggerens navn
                TreeItem<ListItemData> plannerItem = new TreeItem<>(
                        new ListItemData(planner.getId(), formattedPlanner, Specimen.PLANNER));

                // Beregn progresjon og formater prosent
                double progress = planner.getProgress(); // Hent progresjon (0.0 til 1.0)
                String progressPercentage = String.format("%.0f%%", progress * 100);

                // Opprett ProgressBar
                ProgressBar progressBar = new ProgressBar(progress);
                progressBar.setMaxWidth(Double.MAX_VALUE); // Sett progressbaren til å fylle tilgjengelig bredde

                // Opprett en HBox for ProgressBar og prosentandel
                Label progressLabel = new Label(progressPercentage);
                HBox progressBox = new HBox(10, progressBar, progressLabel);
                progressBox.setAlignment(Pos.CENTER_LEFT);
                progressBox.setPadding(new Insets(5, 0, 0, 20)); // Innrykk for å indikere underordnet nivå

                // Opprett TreeItem for progressbaren
                TreeItem<ListItemData> progressItem = new TreeItem<>(new ListItemData("", "", Specimen.OTHER));
                progressItem.setGraphic(progressBox);

                plannerItem.getChildren().add(progressItem);
                dateItem.getChildren().add(plannerItem);
            }

            root.getChildren().add(dateItem);
        }

        deadlinesTreeView.setRoot(root);
    }

    private String daysUntil(LocalDate date) {
        LocalDateTime targetDateTime = date.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(targetDateTime)) {
            return "Date has passed";
        }

        Duration duration = Duration.between(now, targetDateTime);

        long totalDays = duration.toDays();

        if (totalDays >= 1) {
            return String.format("%d days left", totalDays);
        } else {
            return "< 1 day left";
        }
    }

    private void updateActiveTasksList() {
        TreeItem<ListItemData> root = new TreeItem<>(new ListItemData("root", "Root", Specimen.ROOT));
        root.setExpanded(true);

        Map<String, List<Task>> tasksByPlanner = model.getActiveTasks();

        for (Map.Entry<String, List<Task>> entry : tasksByPlanner.entrySet()) {
            String plannerId = entry.getKey();
            String plannerName = model.getPlannerNameById(plannerId);
            PlannerGroup parentGroup = model.getParentGroupByPlannerId(plannerId); // Hent gruppenavn
            String groupName = parentGroup.getGroupName();
            String displayName = String.format("%s - %s", plannerName,
                    groupName != null ? groupName : "No Group"); // Formatter med fallback for null

            List<Task> tasks = entry.getValue();

            TreeItem<ListItemData> plannerItem = new TreeItem<>(
                    new ListItemData(plannerId, displayName, Specimen.PLANNER));
            plannerItem.setExpanded(true);

            for (Task task : tasks) {
                TreeItem<ListItemData> taskItem = new TreeItem<>(
                        new ListItemData(task.getId(), task.getTaskName(), Specimen.TASK));
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

    public Button getRemoveButton() {
        return removeButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public TreeView<ListItemData> getPlansTreeView() {
        return plansTreeView;
    }

    public TreeView<ListItemData> getDeadlinesTreeView() {
        return deadlinesTreeView;
    }

    public TreeView<ListItemData> getActiveTasksTreeView() {
        return activeTasksTreeView;
    }

    public HomeScreenPopupConfigurator getPopup() {
        return popup;
    }

    public Node getRootNode() {
        return root;
    }
}
