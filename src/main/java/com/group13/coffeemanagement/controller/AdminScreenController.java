package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.App;
import com.group13.coffeemanagement.database.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.io.IOException;

public class AdminScreenController {
    @FXML
    private Tab billTab;
    @FXML
    private Tab foodTab;
    @FXML
    private Tab categoryTab;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab accountTab;

    private BillTabController billController;
    private FoodTabController foodController;
    private CategoryTabController categoryController;
    private TableTabController tableController;
    private AccountTabController accountController;

    @FXML
    public Label messageUpdateDB;

    @FXML
    public void initialize() {
        try {
            FXMLLoader billLoader = new FXMLLoader(App.class.getResource("BillTab.fxml"));
            billTab.setContent(billLoader.load());
            billController = billLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupTabHandlers();
    }

    private void setupTabHandlers() {
        billTab.setOnSelectionChanged(event -> {
            if (billTab.isSelected() && billController == null) {
                try {
                    FXMLLoader billLoader = new FXMLLoader(App.class.getResource("BillTab.fxml"));
                    billTab.setContent(billLoader.load());
                    billController = billLoader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        foodTab.setOnSelectionChanged(event -> {
            if (foodTab.isSelected() && foodController == null) {
                try {
                    FXMLLoader foodLoader = new FXMLLoader(App.class.getResource("FoodTab.fxml"));
                    foodTab.setContent(foodLoader.load());
                    foodController = foodLoader.getController();
                    foodController.refreshData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        categoryTab.setOnSelectionChanged(event -> {
            if (categoryTab.isSelected() && categoryController == null) {
                try {
                    FXMLLoader categoryLoader = new FXMLLoader(App.class.getResource("CategoryTab.fxml"));
                    categoryTab.setContent(categoryLoader.load());
                    categoryController = categoryLoader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        tableTab.setOnSelectionChanged(event -> {
            if (tableTab.isSelected() && tableController == null) {
                try {
                    FXMLLoader tableLoader = new FXMLLoader(App.class.getResource("TableTab.fxml"));
                    tableTab.setContent(tableLoader.load());
                    tableController = tableLoader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        accountTab.setOnSelectionChanged(event -> {
            if (accountTab.isSelected() && accountController == null) {
                try {
                    FXMLLoader accountLoader = new FXMLLoader(App.class.getResource("AccountTab.fxml"));
                    accountTab.setContent(accountLoader.load());
                    accountController = accountLoader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void backtoMain() throws IOException {
        try {
            App.setRoot("mainScreen");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error returning to main screen", e.getMessage());
        }
    }

    @FXML
    private void saveDB() {
        try {
            // Save all database changes
            ShopDB.saveShopDB();
            UserDB.saveUserDB();

            // Update UI to show success
            showSuccess("Database saved successfully");

            // Refresh all views to show updated data
            // refreshAllData();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error saving database", e.getMessage());
        }
    }

    private void showSuccess(String message) {
        messageUpdateDB.setText(message);
        messageUpdateDB.setStyle("-fx-text-fill: green;");

        // Clear message after delay
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> messageUpdateDB.setText(""));
        pause.play();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getters for sub-controllers
    public BillTabController getbillController() {
        return billController;
    }

    public FoodTabController getFoodController() {
        return foodController;
    }

    public CategoryTabController getCategoryController() {
        return categoryController;
    }

    public TableTabController getTableController() {
        return tableController;
    }

    public AccountTabController getAccountController() {
        return accountController;
    }

    // // Methods to handle global operations
    // public void handleGlobalSearch(String searchTerm) {
    // // Implement global search across all controllers
    // foodController.handleSearch(searchTerm);
    // categoryController.handleSearch(searchTerm);
    // accountController.handleSearch(searchTerm);
    // }

    public void exportData() {
        // Implement data export functionality
    }

    public void importData() {
        // Implement data import functionality
    }
}

// This MainAdminController implementation:

// 1. Manages the initialization and coordination of all sub-controllers
// 2. Handles loading of FXML for each sub-controller
// 3. Sets up tab selection handlers to refresh data when tabs are selected
// 4. Provides central database saving functionality
// 5. Includes error handling and user feedback
// 6. Offers methods for global operations like search
// 7. Manages communication between controllers
// 8. Includes proper error handling and user feedback mechanisms

// To use this controller, you would need to:

// 1. Create separate FXML files for each sub-controller
// 2. Update the main admin FXML to include references to this controller
// 3. Ensure all sub-controllers implement the necessary refresh methods
// 4. Set up proper event handling between controllers

// Example main admin FXML structure:

// ```xml
// <?xml version="1.0" encoding="UTF-8"?>
// <TabPane xmlns:fx="http://javafx.com/fxml"
// fx:controller="com.group13.coffeemanagement.controller.MainAdminController">
// <tabs>
// <Tab fx:id="billTab" text="Doanh Thu"/>
// <Tab fx:id="foodTab" text="Thức ăn"/>
// <Tab fx:id="categoryTab" text="Danh Mục"/>
// <Tab fx:id="tableTab" text="Bàn"/>
// <Tab fx:id="accountTab" text="Account"/>
// </tabs>

// <VBox>
// <Label fx:id="messageUpdateDB"/>
// <Button onAction="#saveDB" text="Save Database"/>
// <Button onAction="#backtoMain" text="Back to Main"/>
// </VBox>
// </TabPane>
// ```
