package com.myapplication.datacontainers;

import java.util.ArrayList;

public class Category {
    private String name;
    private ArrayList<Item> items = new ArrayList<>();

    /**
     * Creates a category with the name that is passed into it.
     * @param name The name of the category.
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * A good old getter for the name.
     * @return the name specified.
     */
    public String getName() {
        return name;
    }

    /**
     * A good old setter for the name, so the category can have a name.
     * @param name sets the name you specify it!
     */
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
