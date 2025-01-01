package com.martin.dayplanner;

import javafx.application.Application;
import javafx.stage.Stage;
import com.martin.dayplanner.model.Planner;
import com.martin.dayplanner.controller.TaskController;
import com.martin.dayplanner.view.TaskView;

public class DayPlannerApp extends Application {

    @Override
    public void start(Stage stage) {
        Planner planner = new Planner();
        TaskController taskController = new TaskController(planner);
        TaskView taskView = new TaskView(taskController);

        taskView.display(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
