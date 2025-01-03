package com.martin.dayplanner;

import javafx.application.Application;
import javafx.stage.Stage;
import com.martin.dayplanner.model.AppModel;
import com.martin.dayplanner.controller.AppController;
import com.martin.dayplanner.view.AppView;

public class DayPlannerApp extends Application {

    @Override
    public void start(Stage stage) {
        AppModel model = new AppModel();
        AppView view = new AppView(model);
        new AppController(model, view);

        view.display(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
