package com.group13.coffeemanagement.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group13.coffeemanagement.Gson_LocalDateTimeTypeAdapter;
import com.group13.coffeemanagement.model.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.time.LocalDateTime;

public class OrderBillDB {
    public static List<Bill> bills = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();

    private static final String FILE_PATH = "order_bill.json";
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class , new Gson_LocalDateTimeTypeAdapter() ).setPrettyPrinting().create();

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

            if (data == null) {
                return;
            }

            bills = data.bills;
            orders = data.orders;

        } catch (IOException e) {
            System.err.println("ERROR: Could not load bill & order data. " + e.getMessage());
        }
    }

    private static class OrderBillDBData {

        List<Bill> bills;
        List<Order> orders;

        public OrderBillDBData(List<Bill> bills, List<Order> orders) {
            this.bills = bills;
            this.orders = orders;
        }

    }
}
