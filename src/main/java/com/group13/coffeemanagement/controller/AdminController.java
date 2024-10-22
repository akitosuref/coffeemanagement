package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.database.*;
import com.group13.coffeemanagement.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.collections.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminController {

    // Doanh Thu Tab

    @FXML
    private DatePicker orderDatePicker;

    @FXML
    private DatePicker paidDatePicker;

    @FXML
    private TableView<Bill> billTableView;

    @FXML
    private TableColumn<Bill, String> tableNameCol;

    @FXML
    private TableColumn<Bill, Integer> discountCol;

    @FXML
    private TableColumn<Bill, Long> totalPriceCol;

    @FXML
    private TableColumn<Bill, String> orderDateCol;

    @FXML
    private TableColumn<Bill, String> paidDateCol;

    @FXML
    private Button viewButton;

    // Thức ăn Tab

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
    private Label messageUpdateDB;

    @FXML
    public void initialize() {
        // Initialize columns
        tableNameCol.setCellValueFactory(cellData -> {
            // Use a lambda to retrieve the table name based on tableID
            String tableName = getTableNameById(cellData.getValue().getTableID());
            return new SimpleStringProperty(tableName);
        });

        discountCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getDisCount()).asObject());
        totalPriceCol.setCellValueFactory(
                cellData -> new SimpleLongProperty(cellData.getValue().getTotalPrice()).asObject());

        orderDateCol.setCellValueFactory(cellData -> {
            LocalDate orderDate = cellData.getValue().getOrderDate().toLocalDate();
            return new SimpleStringProperty(orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        });

        paidDateCol.setCellValueFactory(cellData -> {
            LocalDate paidDate = cellData.getValue().getPaidDate() != null
                    ? cellData.getValue().getPaidDate().toLocalDate()
                    : null;
            return new SimpleStringProperty(
                    paidDate != null ? paidDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "");
        });

        // Thức ăn code
        foodIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        foodNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        categoryNameCol
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryName()));
        priceCol.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getPrice()).asObject());

        // Initialize the price spinner
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000)); // Adjust max value
                                                                                                     // as needed

        // Load categories into the choice box
        loadCategories();

        // Load food items into the table
        loadFoodItems();

        // Set table selection listener
        foodTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                messageUpdateDB.setText("");
                populateFoodFields(newSelection);
            }
        });
    }

    @FXML
    public void handleViewButton() {
        LocalDate orderDate = orderDatePicker.getValue();
        LocalDate paidDate = paidDatePicker.getValue();

        // Validate dates
        if (orderDate == null || paidDate == null) {
            System.out.println("Please select both dates.");
            return;
        }

        // Clear previous data
        billTableView.getItems().clear();

        // Fetch bills based on the selected date range
        ObservableList<Bill> billsInRange = FXCollections.observableArrayList();

        for (Bill bill : OrderBillDB.bills) {
            LocalDate billOrderDate = bill.getOrderDate().toLocalDate();
            LocalDate billPaidDate = bill.isPaid() ? bill.getPaidDate().toLocalDate() : null;

            if (!billOrderDate.isBefore(orderDate) && !billOrderDate.isAfter(paidDate)) {
                billsInRange.add(bill);
            }
        }

        // Update TableView
        billTableView.setItems(billsInRange);
    }

    private String getTableNameById(int tableId) {
        for (Table table : ShopDB.tables) {
            if (table.getId() == tableId) {
                return table.getName();
            }
        }
        return "Unknown Table"; // Fallback if table not found
    }

    public DatePicker getOrderDatePicker() {
        return orderDatePicker;
    }

    public void setOrderDatePicker(DatePicker orderDatePicker) {
        this.orderDatePicker = orderDatePicker;
    }

    public DatePicker getPaidDatePicker() {
        return paidDatePicker;
    }

    public void setPaidDatePicker(DatePicker paidDatePicker) {
        this.paidDatePicker = paidDatePicker;
    }

    public TableView<Bill> getBillTableView() {
        return billTableView;
    }

    public void setBillTableView(TableView<Bill> billTableView) {
        this.billTableView = billTableView;
    }

    public TableColumn<Bill, String> getTableNameCol() {
        return tableNameCol;
    }

    public void setTableNameCol(TableColumn<Bill, String> tableNameCol) {
        this.tableNameCol = tableNameCol;
    }

    public TableColumn<Bill, Integer> getDiscountCol() {
        return discountCol;
    }

    public void setDiscountCol(TableColumn<Bill, Integer> discountCol) {
        this.discountCol = discountCol;
    }

    public TableColumn<Bill, Long> getTotalPriceCol() {
        return totalPriceCol;
    }

    public void setTotalPriceCol(TableColumn<Bill, Long> totalPriceCol) {
        this.totalPriceCol = totalPriceCol;
    }

    public TableColumn<Bill, String> getOrderDateCol() {
        return orderDateCol;
    }

    public void setOrderDateCol(TableColumn<Bill, String> orderDateCol) {
        this.orderDateCol = orderDateCol;
    }

    public TableColumn<Bill, String> getPaidDateCol() {
        return paidDateCol;
    }

    public void setPaidDateCol(TableColumn<Bill, String> paidDateCol) {
        this.paidDateCol = paidDateCol;
    }

    public Button getViewButton() {
        return viewButton;
    }

    public void setViewButton(Button viewButton) {
        this.viewButton = viewButton;
    }

    // #region Thức ăn
    private void loadFoodItems() {
        ObservableList<Food> foodList = FXCollections.observableArrayList(ShopDB.foods);

        foodTableView.setItems(foodList);

        // Refresh the table view
        foodTableView.refresh();
    }

    private void loadCategories() {
        categoryChoiceBox.getItems().clear();
        categoryChoiceBox.getItems().addAll(ShopDB.categories);
    }

    private void populateFoodFields(Food food) {
        foodIdField.setText(String.valueOf(food.getId()));
        foodNameField.setText(food.getName());
        categoryChoiceBox.setValue(food.getCategory());
        priceSpinner.getValueFactory().setValue((int) (long) food.getPrice());
    }

    @FXML
    private void handleAddFood() {
        clearInputFields();
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

        for (Food food : ShopDB.foods) {
            if (food.getId() == foodId) {
                System.out.println("Food with ID " + foodId + " already exists.");
                food.setName(foodNameField.getText());
                food.setCategoryId(categoryChoiceBox.getValue().getId());
                food.setPrice((long) priceSpinner.getValue());

                // Reload food items
                loadFoodItems();

                return;
            }
        }

        System.out.println("Food with ID " + foodId + " does not exist. Creating new food...");
        Food f = new Food();
        f.setId(foodId);
        f.setName(foodNameField.getText());
        f.setCategoryId(categoryChoiceBox.getValue().getId());
        f.setPrice((long) priceSpinner.getValue());
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

    private void clearInputFields() {
        foodIdField.clear();
        foodNameField.clear();
        categoryChoiceBox.setValue(null);
        priceSpinner.getValueFactory().setValue(0);
    }

    @FXML
    private void saveFoodDB() {
        ShopDB.saveShopDB();
        messageUpdateDB.setText("Cơ sở dữ liệu đã được lưu.");
    }
    // #endregion

}
