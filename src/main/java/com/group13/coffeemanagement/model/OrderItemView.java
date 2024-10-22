package com.group13.coffeemanagement.model;

public class OrderItemView {

    private String foodName;
    private long quantity;
    private long pricePerItem;
    private long totalPrice;
    private int foodID;

    public OrderItemView(String foodName, long quantity, long pricePerItem, int foodID) {
	this.foodName = foodName;
	this.quantity = quantity;
	this.pricePerItem = pricePerItem;
	this.foodID = foodID;
	this.totalPrice = quantity * pricePerItem;
    }

    public int getFoodID() {
	return foodID;
    }

    public void setFoodID(int foodID) {
	this.foodID = foodID;
    }

    public String getFoodName() {
	return foodName;
    }

    public void setFoodName(String foodName) {
	this.foodName = foodName;
    }

    public long getQuantity() {
	return quantity;
    }

    public void setQuantity(long quantity) {
	this.quantity = quantity;
    }

    public long getPricePerItem() {
	return pricePerItem;
    }

    public void setPricePerItem(long pricePerItem) {
	this.pricePerItem = pricePerItem;
    }

    public long getTotalPrice() {
	return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
	this.totalPrice = totalPrice;
    }

}
