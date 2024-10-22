package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.database.OrderBillDB;
import com.group13.coffeemanagement.database.ShopDB;
import com.group13.coffeemanagement.model.Bill;
import com.group13.coffeemanagement.model.Table;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminController {

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

    @FXML
    public void initialize() {
        // Initialize columns
        tableNameCol.setCellValueFactory(cellData -> {
            // Use a lambda to retrieve the table name based on tableID
            String tableName = getTableNameById(cellData.getValue().getTableID());
            return new SimpleStringProperty(tableName);
        });

        discountCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDisCount()).asObject());
        totalPriceCol.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getTotalPrice()).asObject());
        
        orderDateCol.setCellValueFactory(cellData -> {
            LocalDate orderDate = cellData.getValue().getOrderDate().toLocalDate();
            return new SimpleStringProperty(orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        });

        paidDateCol.setCellValueFactory(cellData -> {
            LocalDate paidDate = cellData.getValue().getPaidDate() != null ? cellData.getValue().getPaidDate().toLocalDate() : null;
            return new SimpleStringProperty(paidDate != null ? paidDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "");
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
}
