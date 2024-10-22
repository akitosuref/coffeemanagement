package com.group13.coffeemanagement.model;

import com.group13.coffeemanagement.database.UserDB;
import com.group13.coffeemanagement.enums.UserRole;
import com.group13.coffeemanagement.crypto.AESCryptography;

public class User implements Comparable<User> {

    private String name;
    private Integer userId; // UNIQUE //optimize search nhanh hơn nếu dùng với binary tree...
    private String username;  // UNIQUE
    private String passwordHash;
    private UserRole role = UserRole.NHANVIEN;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;

        userId = UserDB.count + 1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public int compareTo(User other) {

        Integer result = Integer.compare(this.userId, other.userId);

        System.out.println("Result " + result);

        return result;
    }

    public boolean checkPassword(String password) {

        try {
            String rawPassword = AESCryptography.decryptPassword(this.passwordHash);
            
            System.out.println("RAW: " + rawPassword);
            

            if (rawPassword.equals(password)) {
                return true;
            }
            
        } catch (Exception e) {
        }

        return false;
    }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", password=" + passwordHash + ", name=" + name + '}';
    }
}
