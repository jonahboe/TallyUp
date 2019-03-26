package com.myapplication.datacontainers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

/**
 * Holds one instance of a set of inventory. Contains a list of category names, which are in
 * connection with actual category objects and their associated items.
 */
public class Inventory {

    public static final String savedInventoryKey = "SAVED_INVENTORY";
    public static final String savedSoldKey = "SAVED_SOLD";
    public static final String savedShippedKey = "SAVED_SHIPPED";
    private static final String TAG = "Inventory";
    private List<Category> inventory = new ArrayList<>();

    /**
     * Gets a list of the category names held within inventory.
     * @return
     */
    public List<Category> getInventory() {
        return this.inventory;
    }

    /**
     * Setting the categories in the inventory.
     * @param categories is passed in, to set the inventory in the category.
     */
    public void setInventory(List<Category> categories) {
        this.inventory = categories;
    }



    public void sortItems() {
        Collections.sort(inventory, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (Category c : inventory) {
            c.sortItems();
        }
    }

    /**
     * You want to get your category? This is how it's gotten.
     * @return the output if it's not null, otherwise return null.
     */
    public List<String> getCategoriesList() {
        if (this.inventory != null) {
            List<String> output = new ArrayList<>();
            for (Category c : inventory) {
                output.add(c.getName());
            }
            return output;
        }
        return null;
    }

    /**
     * A Hashmap to get the items, to add items to the actual list.
     * @return returns the output if not null.
     */
    public HashMap<String,List<String>> getItemsMap() {
        if (this.inventory != null) {
            HashMap<String,List<String>> output = new HashMap<>();
            for (Category c : inventory) {
                List<String> items = new ArrayList<>();
                for (Item i : c.getItems()) {
                    items.add(i.getName());
                }
                output.put(c.getName(),items);
            }
            return output;
        }
        return null;
    }

    /**
     * A Hashmap to get the item quantity to be able to display to the user.
     * @return returns the output if not null.
     */
    public HashMap<String,List<Integer>> getItemsQuantityMap() {
        if (this.inventory != null) {
            HashMap<String,List<Integer>> output = new HashMap<>();
            for (Category c : inventory) {
                List<Integer> items = new ArrayList<>();
                for (Item i : c.getItems()) {
                    items.add(i.getQuantity());
                }
                output.put(c.getName(),items);
            }
            return output;
        }
        return null;
    }

    /**
     * Return the item object specified
     * @param category
     * @param item
     * @return
     */
    public Item getItem(String category, String item) {
        for (Category c : inventory) {
            if (c.getName().equals(category)) {
                for (Item i : c.getItems()) {
                    if (i.getName().equals(item))
                        return  i;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public Category getCategory(String name) {
        for (Category c : inventory) {
            if (c.getName().equals(name))
                return c;
        }
        return null;
    }

    /**
     * This is actually adding the item for the app to see.
     * @param name the item.
     * @param category the category.
     * @param price the price of said item.
     * @param quantity the amount of that item you have.
     */
    public void addItem(String name, String category, float price, int quantity) {
        for (Category c : inventory) {
            if (c.getName().equals(category)) {
                for (Item i : c.getItems()) {
                    if (i.getName().equals(name)) {
                        i.setQuantity(i.getQuantity() + quantity);
                        return;
                    }
                }
                c.addItem(new Item(name,price,quantity));
                return;
            }
        }
        Category c = new Category(category);
        c.addItem(new Item(name,price,quantity));
        inventory.add(c);
    }

    /**
     * Change the name of a category
     * @param oldName
     * @param newName
     */
    public void renameCategory(String oldName, String newName) {
        String toDelete = null;
        for (Category c : inventory) {
            if (c.getName().equals(newName)) {
                toDelete = oldName;
                for (Item i : this.getCategory(oldName).getItems())
                    c.addItem(i);
            }
        }
        if (toDelete != null) {
            this.deleteCategory(oldName);
            return;
        }

        else {
            for (Category c : inventory) {
                if (c.getName().equals(oldName))
                    c.setName(newName);
            }
        }
    }

    /**
     * Delete a category from the inventory
     * @param category the name of the category to be deleted
     */
    public void deleteCategory(String category) {
        ArrayList<Category> toRemove = new ArrayList<>();
        for (Category c : inventory) {
            if (c.getName().equals(category))
                toRemove.add(c);
        }
        for (Category c : toRemove) {
            inventory.remove(c);
        }
    }

    /**
     * Rename the items of the selected category and names to something else
     * @param category
     * @param oldName
     * @param newName
     */
    public void renameItem(String category, String oldName, String newName) {
        for (Category c : inventory) {
            if (c.getName().equals(category)) {
                for (Item i : c.getItems()) {
                    if (i.getName().equals(oldName))
                        i.setName(newName);
                }
            }
        }
    }

    /**
     * Delete an item from the specified category
     * @param category
     * @param item
     */
    public void deleteItem(String category, String item) {
        for (Category c : inventory) {
            if (c.getName().equals(category)) {
                ArrayList<Item> toRemove = new ArrayList<>();
                for (Item i : c.getItems()) {
                    if (i.getName().equals(item))
                        toRemove.add(i);
                }
                for (Item i : toRemove) {
                    c.removeItem(i);
                }
            }
        }
    }

    /**
     * Saves the inventory, so you don't have to continue to add things when you use the app.
     * @param parentActivity the name of the activity.
     * @param key the key needed.
     */
    public void saveInventory(Activity parentActivity, String key) {
        // Create GSon string
        Gson gson = new Gson();
        Inventory temp = new Inventory();
        temp.setInventory(this.getInventory());
        String jsonString = gson.toJson(temp);
        // Load to preferences
        SharedPreferences sharedPreferences = parentActivity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, jsonString);
        editor.commit();
    }

    /**
     * Load the inventory, so the app loads on startup, to see your beautiful list.
     * @param parentActivity the name of the activity
     * @param key the key needed.
     */
    public void loadInventory(Activity parentActivity, String key) {
        // Load preferences
        SharedPreferences sharedPreferences = parentActivity.getPreferences(MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key, null);

        // Load to GSon
        if (jsonString == null) {
            this.inventory = new ArrayList<>();
        }
        else {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(jsonString));
            Inventory i = gson.fromJson(br, Inventory.class);
            try {
                br.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            this.setInventory(i.getInventory());
        }
    }
}
