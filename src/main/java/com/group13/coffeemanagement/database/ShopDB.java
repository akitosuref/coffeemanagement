package com.group13.coffeemanagement.database;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.group13.coffeemanagement.model.Category;
import com.group13.coffeemanagement.model.Food;
import com.group13.coffeemanagement.model.Table;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShopDB {

    // Public static fields to store data (accessible globally)
    public static List<Table> tables = new ArrayList<>();
    public static List<Food> foods = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();

    // Constants
    private static final String FILE_PATH = "shop_data.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Private constructor to prevent instantiation
    private ShopDB() {
        // This ensures no one creates an object of ShopDB, but the class can be accessed directly.
    }

    // Save the current state of ShopDB to JSON
    public static void saveShopDB() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(new ShopDBData(tables, foods, categories), writer);
            System.out.println("Shop data saved to " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    // Load data from the JSON file into the static fields
    public static void loadShopDB() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type shopDBType = new TypeToken<ShopDBData>() {}.getType();
            ShopDBData data = gson.fromJson(reader, shopDBType);

            // Assign loaded data to static fields
            tables = data.tables;
            foods = data.foods;
            categories = data.categories;

        } catch (IOException e) {
            System.err.println("ERROR: Could not load shop data. " + e.getMessage());
        }
    }

    // Inner static class to represent the structure of data in the JSON file
    private static class ShopDBData {
        List<Table> tables;
        List<Food> foods;
        List<Category> categories;

        // Constructor to initialize the inner class with data
        public ShopDBData(List<Table> tables, List<Food> foods, List<Category> categories) {
            this.tables = tables;
            this.foods = foods;
            this.categories = categories;
        }
    }
}