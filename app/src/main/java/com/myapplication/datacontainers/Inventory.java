package com.myapplication.datacontainers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Inventory {

    public static final String savedInventoryKey = "SAVED_INVENTORY";
    public static final String savedSoldKey = "SAVED_SOLD";
    public static final String savedShippedKey = "SAVED_SHIPPED";
    private static final String TAG = "Inventory";

    private List<Category> inventory;

    public List<Category> getInventory() {
        return this.inventory;
    }

    public void setInventory(List<Category> categories) {
        this.inventory = categories;
    }

    public List<String> getCategories() {
        if (this.inventory != null) {
            List<String> output = new ArrayList<>();
            for (Category c : inventory) {
                output.add(c.getName());
            }
            return output;
        }
        return null;
    }

    public HashMap<String,List<String>> getItems() {
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

    public void addItem(String name, String cat , float price, int quantity) {
        for (Category c : inventory) {
            if (c.getName().equals(cat)) {
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
        Category c = new Category(cat);
        c.addItem(new Item(name,price,quantity));
        inventory.add(c);

    }

    public void removeItem() {

    }

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
