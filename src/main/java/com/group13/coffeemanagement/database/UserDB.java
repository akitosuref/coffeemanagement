package com.group13.coffeemanagement.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.group13.coffeemanagement.model.User;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDB {

    public static int count = 0;
    public static List<User> users = new ArrayList<>();

    public static boolean isAdmin = false;

    private static final String FILE_PATH = "users.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private UserDB() {
        
    }

    public static void saveUserDB() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(new UserDBData(count, users), writer);
            System.out.println("Users data saved to " + FILE_PATH);
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }

    // Load a list of User objects from a JSON file
    public static void loadUserDB() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type userDBType = new TypeToken<UserDBData>() {
            }.getType();

            UserDBData data = gson.fromJson(reader, userDBType);

            count = data.count;
            users = data.users;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int checkUserExist(User user) {
        Collections.sort(users); //sort trước

        int index = Collections.binarySearch(users, user);

        if (index >= 0) {
            System.out.println("User đã tồn tại");
            return index;
        } else {

            System.out.println("Thêm user: " + user.getName());

            users.add(user);

            return index;
        }
    }

    private static class UserDBData {

        int count;
        List<User> users;

        public UserDBData(int count, List<User> users) {
            this.count = count;
            this.users = users;
        }
    }
}
