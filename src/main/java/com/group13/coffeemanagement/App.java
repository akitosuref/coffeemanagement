package com.group13.coffeemanagement;

import com.group13.coffeemanagement.database.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        // UserDB.isAdmin = true;
        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.setTitle("Coffee Management");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);

        switch (fxml) {
            case "login":
                scene.getWindow().setWidth(600);
                scene.getWindow().setHeight(400);
                break;
            case "mainScreen":
                scene.getWindow().setWidth(1050);
                scene.getWindow().setHeight(650);
                break;
            case "AdminScreen":
                scene.getWindow().setWidth(1000);
                scene.getWindow().setHeight(650);
                break;
            default:
                scene.getWindow().setWidth(600);
                scene.getWindow().setHeight(400);
                break;
        }

    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // Load Database
        ShopDB.loadShopDB();
        UserDB.loadUserDB();
        OrderBillDB.loadBill();
        // System.out.println("Loaded Tables:" + ShopDB.tables);
        // System.out.println("Loaded Users: " + UserDB.users);

        launch();
    }

}
