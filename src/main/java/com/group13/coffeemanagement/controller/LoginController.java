package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.App;
import com.group13.coffeemanagement.database.UserDB;
import com.group13.coffeemanagement.enums.UserRole;
import com.group13.coffeemanagement.model.User;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void gotoView() throws IOException {

        App.setRoot("mainScreen");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (checkPassword(username, password)) {
            messageLabel.setText("Login successful!");
            messageLabel.setStyle("-fx-text-fill: green;");

            try {
                gotoView();
            } catch (IOException e) {
                System.out.println("ERROR: " + e);
            }
        } else {
            messageLabel.setText("Invalid username or password.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean checkPassword(String username, String password) {
        List<User> users = UserDB.users;

        for (User u : users) {
            if (u.getUsername().equals(username)) {
                if (u.checkPassword(password)) {

                    if (u.getRole() == UserRole.QUANLY) {
                        UserDB.isAdmin = true;
                    }

                    return true;
                }
            }
        }
        return false;
    }
}
