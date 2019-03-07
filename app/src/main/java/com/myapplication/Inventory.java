package com.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Inventory {

    private List<Category> categories;

    private String localKey = "SAVED_INVENTORY";

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void saveInventory(List<Category> list, Activity a) {
        // Create GSon string
        categories = list;
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);

        // Load to preferences
        SharedPreferences sharedPreferences = a.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(localKey, jsonString);
        editor.commit();
    }

    public List<Category> loadInventory(Activity a) {
        // Load preferences
        SharedPreferences sharedPreferences = a.getPreferences(MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(localKey, null);

        // Load to GSon
        Inventory i;
        if (jsonString == null) {
            List<Category> c = new ArrayList<>();
            return c;
        }
        else {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(jsonString));
            i = gson.fromJson(br, Inventory.class);
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return i.getCategories();
    }
}
