package com.group13.coffeemanagement.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group13.coffeemanagement.model.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderBillDB {
    public static List<Bill> bills = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();

    private static final String FILE_PATH = "order_bill.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private OrderBillDB() {
    }

    // Save the current state of ShopDB to JSON
    public static void saveBill() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(new OrderBillDBData(bills, orders), writer);
            System.out.println("Bill & Order data saved to " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    // Load data from the JSON file into the static fields
    public static void loadBill() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type shopDBType = new TypeToken<OrderBillDBData>() {
            }.getType();
            OrderBillDBData data = gson.fromJson(reader, shopDBType);

            // Assign loaded data to static fields
            bills = data.bills;
            orders = data.orders;

        } catch (IOException e) {
            System.err.println("ERROR: Could not load bill & order data. " + e.getMessage());
        }
    }

    // Inner static class to represent the structure of data in the JSON file
    private static class OrderBillDBData {

        List<Bill> bills;
        List<Order> orders;

        public OrderBillDBData(List<Bill> bills, List<Order> orders) {
            this.bills = bills;
            this.orders = orders;
        }

    }
}
