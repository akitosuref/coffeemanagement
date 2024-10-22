package com.group13.coffeemanagement.model;

import com.group13.coffeemanagement.database.OrderBillDB;

import java.util.List;

public class Bill {

    private int id;
    private int tableID;
    private int disCount;
    private boolean paid;
    private long totalPrice;

    public Bill() {
	id = OrderBillDB.bills.size() + 1;
	paid = false;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public boolean isPaid() {
	return paid;
    }

    public void setPaid(boolean paid) {
	this.paid = paid;
    }

    public int getTableID() {
	return tableID;
    }

    public void setTableID(int tableID) {
	this.tableID = tableID;
    }

    public int getDisCount() {
	return disCount;
    }

    public void setDisCount(int disCount) {
	this.disCount = disCount;
    }

    public long getTotalPrice() {
	return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
	this.totalPrice = totalPrice;
    }

}
