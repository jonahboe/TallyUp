package com.myapplication.datacontainers;

import java.util.ArrayList;

/**
 * A category which holds one set of items for an inventory to draw from.
 */
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

    /**
     * A getter for the list of items in each category.
     * @return the items specified
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * A setter to set the item into the list for the category.
     * @param items sets the item, it's how you set the items.
     */
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * Items can be added to the list.
     * @param i passing in the item that will be added.
     */
    public void addItem(Item i) {
        items.add(i);
    }

    /**
     * Items can be remove from the list.
     * @param i passing in the item to be removed.
     */
    public void removeItem(Item i) {
        items.remove(i);
    }
}
