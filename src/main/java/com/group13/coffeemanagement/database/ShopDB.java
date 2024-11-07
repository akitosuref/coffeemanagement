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

    public static List<Table> tables = new ArrayList<>();
    public static List<Food> foods = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();

    private static final String FILE_PATH = "shop_data.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ShopDB() {
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

            if (data == null) {
                return;
            }

            tables = data.tables;
            foods = data.foods;
            categories = data.categories;

        } catch (IOException e) {
            System.err.println("ERROR: Could not load shop data. " + e.getMessage());
        }
    }

    private static class ShopDBData {
        List<Table> tables;
        List<Food> foods;
        List<Category> categories;

        public ShopDBData(List<Table> tables, List<Food> foods, List<Category> categories) {
            this.tables = tables;
            this.foods = foods;
            this.categories = categories;
        }
    }
}