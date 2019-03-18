package com.myapplication.datacontainers;

import java.util.ArrayList;

public class Category {
    private String name;
    private ArrayList<Item> items = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    void addItem(Item i) {
        items.add(i);
    }

    void removeItem(Item i) {
        items.remove(i);
    }
}
