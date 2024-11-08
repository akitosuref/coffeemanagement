package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.database.ShopDB;
import com.group13.coffeemanagement.model.Table;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TableTabController {
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

    @FXML
    private void initialize() {
        System.out.println("Table tab initialized");
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
                populateTableFields(newSelection);
            }
        });
    }

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
}
