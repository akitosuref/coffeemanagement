
package com.group13.coffeemanagement.model;

import com.group13.coffeemanagement.database.ShopDB;

public class Food {
    private int id;
    private String name;
    private long price;
    private int categoryId;
    private String imgName;

    public Food(String name, long price, int categoryId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public Food() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        for (Category category : ShopDB.categories) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    public String getCategoryName() {
        return getCategory().getName();
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    @Override
    public String toString() {
        return "Food{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + ", categoryId=" + categoryId
                + '}';
    }

}
