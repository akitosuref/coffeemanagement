package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.database.*;
import com.group13.coffeemanagement.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.collections.*;

public class CategoryTabController {
    @FXML
    public TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, Integer> categoryIdCol;

    @FXML
    private TableColumn<Category, String> categoryNameCol2;

    @FXML
    private TextField categoryIdField;

    @FXML
    private TextField categoryNameField;

    @FXML
    private TextField searchCategoryField;

    @FXML
    private void initialize() {
        System.out.println("Category tab initialized");

        loadCategories();

        categoryIdCol
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        categoryNameCol2
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        // Set table selection listener
        categoryTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateCategoryFields(newSelection);
            }
        });
    }

    public void loadCategories() {
        ObservableList<Category> categoryList = FXCollections.observableArrayList(ShopDB.categories);

        categoryTableView.setItems(categoryList);

        // Refresh the table view
        categoryTableView.refresh();

    }

    private void populateCategoryFields(Category category) {
        categoryIdField.setText(String.valueOf(category.getId()));
        categoryNameField.setText(category.getName());
    }

    @FXML
    private void handleSearchCategory() {
        String query = searchCategoryField.getText().toLowerCase();
        ObservableList<Category> filteredList = FXCollections.observableArrayList();

        for (Category category : ShopDB.categories) {
            if (category.getName().toLowerCase().contains(query)) {
                filteredList.add(category);
            }
        }

        categoryTableView.setItems(filteredList);
    }

    @FXML
    private void handleAddCategory() {
        Category c = new Category();
        c.setId(ShopDB.categories.size() + 1);
        c.setName("Loại " + (ShopDB.categories.size() + 1));
        ShopDB.categories.add(c);
        loadCategories();
    }

    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            ShopDB.categories.remove(selectedCategory);
            loadCategories();
        }
    }

    @FXML
    private void handleSaveCategory() {
        Category selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            selectedCategory.setName(categoryNameField.getText());
            loadCategories();
        }
    }
}
