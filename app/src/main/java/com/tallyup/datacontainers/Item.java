package com.tallyup.datacontainers;

/**
 * One item within a category of inventory. Though it is one item, it can have a varying quantity.
 */
public class Item {

    String name;
    int quantity;

    /**
     * What does an item have?
     * @param name It's name.
     * @param quantity How many?
     */
    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * Able to get name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Able to set the name.
     * @param name is the name that is to be set for the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Able to get the quantity.
     * @return how many of that item you have.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Able to set the quantity.
     * @param quantity is the amount of that particular item you have.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
