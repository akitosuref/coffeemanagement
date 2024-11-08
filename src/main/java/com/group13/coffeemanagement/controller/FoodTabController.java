package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.database.*;
import com.group13.coffeemanagement.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.beans.property.*;
import javafx.collections.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FoodTabController {
    @FXML
    private TableView<Food> foodTableView;

    @FXML
    private TableColumn<Food, Integer> foodIdCol;

    @FXML
    private TableColumn<Food, String> foodNameCol;

    @FXML
    private TableColumn<Food, String> categoryNameCol;

    @FXML
    private TableColumn<Food, Long> priceCol;

    @FXML
    private TextField foodIdField;

    @FXML
    private TextField foodNameField;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;

    @FXML
    private Spinner<Integer> priceSpinner;

    @FXML
    private TextField searchField;

    @FXML
    private TextField foodImgPathField;

    @FXML
    private ImageView foodImg;

    @FXML
    private Button saveButton;

    public void refreshData() {
        loadFoodItems();
    }

    @FXML
    public void initialize() {
        System.out.println("Initialize food tab controller");
        // Thức ăn code
        foodIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        foodNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        categoryNameCol
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryName()));
        priceCol.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getPrice()).asObject());

        // Initialize the price spinner
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000)); // Adjust max
                                                                                                      // value
                                                                                                      // as needed

        // Load categories into the choice box
        loadCategories();

        // Load food items into the table
        loadFoodItems();

        setDisableLabelInputButtons(true);

        // Set table selection listener
        foodTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setDisableLabelInputButtons(false);
                populateFoodFields(newSelection);
            }
        });

        foodImg.setOnMouseClicked(event -> {
            File selectedFile = chooseFoodImg();

            if (selectedFile == null)
                return;

            saveImageToFolder(selectedFile);

            loadImageFromAppImages(selectedFile.getName());
        });
    }

    private void loadFoodItems() {
        ObservableList<Food> foodList = FXCollections.observableArrayList(ShopDB.foods);

        foodTableView.setItems(foodList);

        // Refresh the table view
        foodTableView.refresh();
    }

    public void loadCategories() {
        // ObservableList<Category> categoryList =
        // FXCollections.observableArrayList(ShopDB.categories);

        // cateTabController.categoryTableView.setItems(categoryList);

        // // Refresh the table view
        // cateTabController.categoryTableView.refresh();

        categoryChoiceBox.getItems().clear();
        categoryChoiceBox.getItems().addAll(ShopDB.categories);
    }

    private void populateFoodFields(Food food) {
        foodIdField.setText(String.valueOf(food.getId()));
        foodNameField.setText(food.getName());
        categoryChoiceBox.setValue(food.getCategory());
        priceSpinner.getValueFactory().setValue((int) (long) food.getPrice());

        for (Food f : ShopDB.foods) {
            if (f.getId() == food.getId()) {

                if (f.getImgName() == null) {
                    foodImg.setImage(null);
                    foodImgPathField.setText("No image");
                } else {
                    foodImgPathField.setText(f.getImgName());
                    loadImageFromAppImages(f.getImgName());
                }

                break;
            }
        }
    }

    @FXML
    private void handleAddFood() {
        clearInputFields();
        setDisableLabelInputButtons(false);
        foodIdField.setText(String.valueOf(ShopDB.foods.size() + 1)); // Auto-increment ID for new food
    }

    @FXML
    private void handleSaveFood() {

        if (foodNameField.getText().isEmpty()) {
            System.out.println("Please enter food name.");
            return;
        }

        if (categoryChoiceBox.getValue() == null) {
            System.out.println("Please select a category.");
            return;
        }

        if (priceSpinner.getValue() == 0 || priceSpinner.getValue() == null) {
            System.out.println("Please enter a price.");
            return;
        }

        if (foodIdField.getText().isEmpty()) {
            System.out.println("Please enter a food ID.");
            return;
        }

        int foodId = Integer.parseInt(foodIdField.getText());

        // Save new food info
        for (Food food : ShopDB.foods) {
            if (food.getId() == foodId) {
                System.out.println("Updating existing food id:" + foodId);
                food.setName(foodNameField.getText());
                food.setCategoryId(categoryChoiceBox.getValue().getId());
                food.setPrice((long) priceSpinner.getValue());
                food.setImgName(foodImgPathField.getText());

                // Reload food items
                loadFoodItems();

                return;
            }
        }

        // Add new food
        System.out.println("Food with ID " + foodId + " does not exist. Creating new food...");
        Food f = new Food();
        f.setId(foodId);
        f.setName(foodNameField.getText());
        f.setCategoryId(categoryChoiceBox.getValue().getId());
        f.setPrice((long) priceSpinner.getValue());
        f.setImgName(foodImgPathField.getText());
        ShopDB.foods.add(f);

        clearInputFields();
        // Reload food items
        loadFoodItems();
    }

    @FXML
    private void handleDeleteFood() {
        Food selectedFood = foodTableView.getSelectionModel().getSelectedItem();
        if (selectedFood != null) {
            ShopDB.foods.remove(selectedFood);
            loadFoodItems();
            clearInputFields();
        }
    }

    @FXML
    private void handleSearchFood() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Food> filteredList = FXCollections.observableArrayList();

        for (Food food : ShopDB.foods) {
            if (food.getName().toLowerCase().contains(query)) {
                filteredList.add(food);
            }
        }

        foodTableView.setItems(filteredList);
    }

    private void loadImageFromAppImages(String imgName) {
        File imageFile = new File("images/" + imgName);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            foodImg.setImage(image);
        } else {
            foodImg.setImage(null);
            System.out.println("Image file not found at: " + imageFile.getPath());
        }
    }

    private void saveImageToFolder(File sourceFile) {
        if (sourceFile != null) {
            // Define the destination folder within your project
            File destinationFolder = new File("images");

            // Create the directory if it doesn't exist
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            // Define the destination file path
            File destinationFile = new File(destinationFolder, sourceFile.getName());

            try {
                // Copy the selected file to the images folder
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image saved to: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File chooseFoodImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            foodImgPathField.setText(selectedFile.getName());
        }
        // else {
        // selectedFile = new File("images", foodImgPathField.getText());
        // }

        return selectedFile;
    }

    private void clearInputFields() {
        foodIdField.clear();
        foodNameField.clear();
        categoryChoiceBox.setValue(null);
        priceSpinner.getValueFactory().setValue(0);
    }

    private void setDisableLabelInputButtons(boolean b) {
        foodIdField.setDisable(b);
        foodNameField.setDisable(b);
        categoryChoiceBox.setDisable(b);
        priceSpinner.setDisable(b);
        // foodImgPathField.setDisable(b);
        foodImg.setDisable(b);
        saveButton.setDisable(b);
    }
}