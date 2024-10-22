
package com.group13.coffeemanagement.model;

import com.group13.coffeemanagement.enums.TableStatus;

public class Table {
    private int id;
    private String name;
    private TableStatus status = TableStatus.NO_USE;

    public Table(int id, String name, TableStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
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

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
    
    
    @Override
    public String toString() {
        return "Table{" + "name='" + name + '\'' + ", status=" + status + '}';
    }
}
