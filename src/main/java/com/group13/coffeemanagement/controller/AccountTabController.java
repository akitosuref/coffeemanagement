package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.crypto.AESCryptography;
import com.group13.coffeemanagement.database.UserDB;
import com.group13.coffeemanagement.model.User;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AccountTabController {
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
    private void initialize() {
        System.out.println("Account tab initialized.");

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
}
