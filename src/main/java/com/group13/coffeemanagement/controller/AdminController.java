package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.App;
import com.group13.coffeemanagement.crypto.AESCryptography;
import com.group13.coffeemanagement.database.*;
import com.group13.coffeemanagement.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.collections.*;

import java.io.IOException;
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

    // Danh Mục Tab

    @FXML
    private TableView<Category> categoryTableView;

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

    // Bàn Tab

    @FXML
    private TableView<Table> tableView;

    @FXML
    private TableColumn<Table, Integer> tableIdCol;

    @FXML
    private TableColumn<Table, String> tableNameCol2;

    @FXML
    private TableColumn<Table, String> tableStatusCol;

    @FXML
    private TextField tableIdField;

    @FXML
    private TextField tableNameField;

    // Account Tab
    @FXML
    private TextField searchAccountField;

    @FXML
    private TableView<User> accountTableView;

    @FXML
    private TableColumn<User, Integer> accountIdCol;

    @FXML
    private TableColumn<User, String> accountNameCol;

    @FXML
    private TableColumn<User, String> accountUserNameCol;

    @FXML
    private TableColumn<User, String> accountRoleCol;

    @FXML
    private TextField accountIdField;

    @FXML
    private TextField accountUserNameField;

    @FXML
    private TextField accountNameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button togglePass;

    private boolean isPasswordVisible = false; // Track password visibility
    private String actualPassword = ""; // Store the real password

    @FXML
    private Button saveAccountButton;

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

        // Danh Mục code

        categoryIdCol
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        categoryNameCol2
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        // Set table selection listener
        categoryTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                messageUpdateDB.setText("");
                populateCategoryFields(newSelection);
            }
        });

        loadTables();

        // Table code
        tableIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tableNameCol2
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        tableStatusCol
                .setCellValueFactory(cellData -> {
                    String statusText;
                    switch (cellData.getValue().getStatus()) {
                        case NO_USE:
                            statusText = "Đang trống";
                            break;
                        case USED:
                            statusText = "Đang sử dụng";
                            break;
                        default:
                            statusText = "";
                            break;
                    }

                    return new SimpleStringProperty(statusText);
                });

        // Set table selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                messageUpdateDB.setText("");
                populateTableFields(newSelection);
            }
        });

        loadAccounts();

        // Account code

        accountIdCol
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());

        accountNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        accountUserNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        accountRoleCol
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().toString()));

        accountTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                messageUpdateDB.setText("");
                try {
                    populateAccountFields(newSelection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        saveAccountButton.setDisable(true);

    }

    @FXML
    private void backtoMain() throws IOException {
        App.setRoot("mainScreen");
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
        ObservableList<Category> categoryList = FXCollections.observableArrayList(ShopDB.categories);

        categoryTableView.setItems(categoryList);

        // Refresh the table view
        categoryTableView.refresh();

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
    private void saveDB() {
        ShopDB.saveShopDB();
        UserDB.saveUserDB();
        messageUpdateDB.setText("Cơ sở dữ liệu đã được lưu.");
    }
    // #endregion

    // #region Danh mục

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

    // #endregion

    // #region Bàn

    private void loadTables() {
        ObservableList<Table> tables = FXCollections.observableArrayList(ShopDB.tables);

        tableView.setItems(tables);

        // Refresh the table view
        tableView.refresh();

        tableView.getItems().clear();
        tableView.getItems().addAll(ShopDB.tables);
    }

    private void populateTableFields(Table table) {
        tableIdField.setText(String.valueOf(table.getId()));
        tableNameField.setText(table.getName());
    }

    @FXML
    private void handleAddTable() {
        Table t = new Table();
        t.setId(ShopDB.tables.size() + 1);
        t.setName("Bàn " + (ShopDB.tables.size() + 1));
        ShopDB.tables.add(t);
        loadTables();
    }

    @FXML
    private void handleDeleteTable() {
        Table selectedTable = tableView.getSelectionModel().getSelectedItem();
        if (selectedTable != null) {
            ShopDB.tables.remove(selectedTable);
            loadTables();
        }
    }

    @FXML
    private void handleSaveTable() {
        Table selectedTable = tableView.getSelectionModel().getSelectedItem();
        if (selectedTable != null) {
            selectedTable.setName(tableNameField.getText());
            loadTables();
        }
    }

    // #endregion

    // #region Account

    @FXML
    private void handleTogglePass() {
        if (isPasswordVisible) {
            actualPassword = passwordField.getText();
            passwordField.setText(maskPassword(actualPassword));
            togglePass.setText("Show");
            isPasswordVisible = false;
            saveAccountButton.setDisable(true);
        } else {
            passwordField.setText(actualPassword);
            togglePass.setText("Hide");
            isPasswordVisible = true;
            saveAccountButton.setDisable(false);
        }
    }

    private String maskPassword(String password) {
        return "*".repeat(password.length()); // Replace each character with '*'
    }

    private void populateAccountFields(User account) throws Exception {
        accountIdField.setText(String.valueOf(account.getUserId()));
        accountUserNameField.setText(account.getUsername());
        accountNameField.setText(account.getName());

        String passwordHash = account.getPasswordHash();
        String decryptPassword = AESCryptography.decryptPassword(passwordHash);
        String maskPassword = maskPassword(decryptPassword);
        passwordField.setText(maskPassword);

        togglePass.setText("Show");
        isPasswordVisible = false;

        actualPassword = decryptPassword;
    }

    private void loadAccounts() {
        ObservableList<User> accounts = FXCollections.observableArrayList(UserDB.users);
        accountTableView.setItems(accounts);

        // Refresh the table view
        accountTableView.refresh();

        accountTableView.getItems().clear();
        accountTableView.getItems().addAll(UserDB.users);
    }

    @FXML
    private void handleSearchAccount() {
        String query = searchAccountField.getText().toLowerCase();
        ObservableList<User> filteredList = FXCollections.observableArrayList();

        for (User user : UserDB.users) {
            if (user.getName().toLowerCase().contains(query)) {
                filteredList.add(user);
            }

        }

        accountTableView.setItems(filteredList);
    }

    @FXML
    private void handleAddAccount() {
        User u = new User();
        u.setUserId(UserDB.users.size() + 1);
        u.setName("Tài khoản " + (UserDB.users.size() + 1));
        UserDB.users.add(u);

        loadAccounts();
    }

    @FXML
    private void handleDeleteAccount() {
        User selectedAccount = accountTableView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            UserDB.users.remove(selectedAccount);
            loadAccounts();
        }
    }

    @FXML
    private void handleSaveAccount() throws Exception {
        User selectedAccount = accountTableView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            for (User user : UserDB.users) {
                if (selectedAccount.getUserId() == user.getUserId()) {
                    user.setUsername(accountUserNameField.getText());
                    user.setName(accountNameField.getText());
                    String password = passwordField.getText();
                    String encryptPassword = AESCryptography.encryptPassword(password);
                    user.setPasswordHash(encryptPassword);

                    loadAccounts();

                    // Reset the fields
                    accountIdField.setText("");
                    accountUserNameField.setText("");
                    accountNameField.setText("");
                    passwordField.setText("");
                    togglePass.setText("Show");
                    isPasswordVisible = false;
                    saveAccountButton.setDisable(true);
                    break;
                }
            }
        }
    }

    // #endregion

}
