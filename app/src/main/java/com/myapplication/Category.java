package com.myapplication;

import java.util.ArrayList;

public class Category extends Item {
    String name;
    ArrayList<Item> items;

    @Override
    public String getName() {
        return name;
    }

    @Override
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
