package com.martin.dayplanner.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.martin.dayplanner.model.storage.StorageHandler;

import java.io.FileWriter;
import java.io.IOException;

public class HomeScreenTest {

    private StorageHandler storageHandler;
    private HomeScreen homeScreen;
    private AppModel appModel;

    @Before
    public void setUp() throws IOException {
        // Nullstill filene
        try (FileWriter writer = new FileWriter("planners.json")) {
            writer.write("{}");
        }
        appModel = new AppModel();
        storageHandler = new StorageHandler();
        homeScreen = new HomeScreen(appModel, storageHandler);
    }

    @After
    public void tearDown() throws IOException {
        // Rydd opp filene etter testene
        try (FileWriter writer = new FileWriter("planners.json")) {
            writer.write("{}");
        }
    }

    @Test
    public void testAddPlanner() {
        homeScreen.addPlanner("NewPlanner");
        assertNotNull(homeScreen.findPlannerByName("NewPlanner"));
    }

    @Test
    public void testRemovePlanner() {
        homeScreen.addPlanner("ToBeRemoved");
        homeScreen.removePlanner("ToBeRemoved");
        assertNull(homeScreen.findPlannerByName("ToBeRemoved"));
    }
}
