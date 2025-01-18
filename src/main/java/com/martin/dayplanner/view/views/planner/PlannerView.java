package com.martin.dayplanner.view.views.planner;

import com.martin.dayplanner.model.task.Task;
import com.martin.dayplanner.model.task.TaskStatus;
import com.martin.dayplanner.view.views.BaseView;
import com.martin.dayplanner.view.views.ListItemData;
import com.martin.dayplanner.view.views.Specimen;
import com.martin.dayplanner.view.views.Viewable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class PlannerView extends BaseView implements Viewable {

    private final ViewablePlanner model;
    private final PlannerPopupConfigurator popup;
    private final Button addTaskButton;
    private final Button removeTaskButton;
    private final Button editTaskButton;
    private final Button goToMenuButton;
    private final ListView<ListItemData> newTasksListView;
    private final ListView<ListItemData> pendingTasksListView;
    private final ListView<ListItemData> completedTasksListView;
    private final BorderPane root;

    public PlannerView(ViewablePlanner model) {
        this.model = model;
        this.popup = new PlannerPopupConfigurator(model);

        addTaskButton = createButton("Add Task");
        removeTaskButton = createButton("Remove Task");
        editTaskButton = createButton("Edit Task");
        goToMenuButton = createButton("Go To Menu");

        newTasksListView = new ListView<>();
        pendingTasksListView = new ListView<>();
        completedTasksListView = new ListView<>();

        setupListView(newTasksListView);
        setupListView(pendingTasksListView);
        setupListView(completedTasksListView);

        root = new BorderPane();
        updateTaskLists();
        setupLayout();
    }

    private void setupListView(ListView<ListItemData> listView) {
        listView.setCellFactory(param -> {
            ListCell<ListItemData> cell = new ListCell<>() {
                @Override
                protected void updateItem(ListItemData item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                        setWrapText(true);
                        setPrefWidth(listView.getWidth() - 20); // Adjust width to avoid horizontal scroll
                    }
                }
            };
            return cell;
        });
        listView.setPrefWidth(0); // Disable horizontal scrolling
    }

    private void setupLayout() {
        root.setTop(createTopSection(model.getPlannerName()));
        root.setCenter(createCenterSection());
        root.setBottom(createButtonRow());
    }

    private GridPane createCenterSection() {
        GridPane taskGrid = new GridPane();
        taskGrid.setPadding(new Insets(10, 10, 10, 10));
        taskGrid.setHgap(5);
        taskGrid.setVgap(5);
        taskGrid.getStyleClass().add("center-grid");

        taskGrid.add(createSection("Not Started", newTasksListView, "plans-box"), 0, 0);
        taskGrid.add(createSection("Active Tasks", pendingTasksListView, "plans-box"), 1, 0);
        taskGrid.add(createSection("Completed Tasks", completedTasksListView, "plans-box"), 2, 0);

        return taskGrid;
    }

    private HBox createButtonRow() {
        HBox buttonRow = new HBox(10, goToMenuButton, removeTaskButton, editTaskButton, addTaskButton);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setPadding(new Insets(10));
        buttonRow.getStyleClass().add("button-row");
        return buttonRow;
    }

    public void updateTaskLists() {
        newTasksListView.getItems().setAll(getTaskItems(TaskStatus.NOTSTARTED));
        pendingTasksListView.getItems().setAll(getTaskItems(TaskStatus.ACTIVE));
        completedTasksListView.getItems().setAll(getTaskItems(TaskStatus.COMPLETED));
    }

    private List<ListItemData> getTaskItems(TaskStatus status) {
        return model.getTasksByStatus(status).stream()
                .sorted(Comparator.comparing(Task::getPriority).reversed()
                        .thenComparing(Task::getTaskName)) // Sort by priority first, then alphabetically
                .map(task -> new ListItemData(task.getId(), task.getTaskName(), Specimen.TASK))
                .collect(Collectors.toList());
    }

    @Override
    public BorderPane getLayout() {
        return root;
    }

    public List<ListView<ListItemData>> getTaskLists() {
        List<ListView<ListItemData>> allTaskLists = new ArrayList<>();
        allTaskLists.add(newTasksListView);
        allTaskLists.add(pendingTasksListView);
        allTaskLists.add(completedTasksListView);
        return allTaskLists;
    }

    public String getPlannerId() {
        return model.getId();
    }

    public Button getEditTaskButton() {
        return editTaskButton;
    }

    public Button getRemoveTaskButton() {
        return removeTaskButton;
    }

    public Button getAddTaskButton() {
        return addTaskButton;
    }

    public Button getGoToMenuButton() {
        return goToMenuButton;
    }

    public PlannerPopupConfigurator getPopup() {
        return popup;
    }
}
