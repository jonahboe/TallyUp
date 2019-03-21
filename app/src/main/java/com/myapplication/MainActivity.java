package com.myapplication;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.myapplication.datacontainers.Category;
import com.myapplication.datacontainers.Inventory;
import com.myapplication.datacontainers.Item;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddItemDialog.AddItemDialogListener, ExpandableListAdapter.InfoButtonListener {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private boolean areInventoryView = true;
    private static Inventory inventory;
    private static Inventory sold;
    private static Inventory shipped;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_inventory:
                    listAdapter.setDataCategory(inventory.getCategories());
                    listAdapter.setDataItem(inventory.getItems());
                    listAdapter.setDataItemQuantity(inventory.getItemsQuantity());
                    listAdapter.notifyDataSetChanged();
                    areInventoryView = true;
                    return true;
                case R.id.navigation_sold:
                    listAdapter.setDataCategory(sold.getCategories());
                    listAdapter.setDataItem(sold.getItems());
                    listAdapter.setDataItemQuantity(sold.getItemsQuantity());
                    listAdapter.notifyDataSetChanged();
                    areInventoryView = false;
                    return true;
                case R.id.navigation_shipped:
                    listAdapter.setDataCategory(shipped.getCategories());
                    listAdapter.setDataItem(shipped.getItems());
                    listAdapter.setDataItemQuantity(shipped.getItemsQuantity());
                    listAdapter.notifyDataSetChanged();
                    areInventoryView = false;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Setup our menu
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Load saved inventory
        inventory = new Inventory();
        inventory.loadInventory(this, Inventory.savedInventoryKey);

        // Load saved sold list
        sold = new Inventory();
        sold.loadInventory(this, Inventory.savedSoldKey);

        // Load saved shipped list
        shipped = new Inventory();
        shipped.loadInventory(this, Inventory.savedShippedKey);

        // Setup the list view
        listView = findViewById(R.id.expandable_list);
        listAdapter = new ExpandableListAdapter(this,inventory.getCategories(),inventory.getItems(),inventory.getItemsQuantity());
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        inventory.saveInventory(this, Inventory.savedInventoryKey);
        sold.saveInventory(this, Inventory.savedSoldKey);
        shipped.saveInventory(this, Inventory.savedShippedKey);
    }


    // Menu bar stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add our custom menu items
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the item that called this function
        int id = item.getItemId();

        // Check if it was the add button that was pressed
        if (id == R.id.add_item_button) {
            AddItemDialog addItemDialog = new AddItemDialog();
            addItemDialog.show(getSupportFragmentManager(),"addItemDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddedItem(String item, String category, float price, int quantity) {

        inventory.addItem(item,category,price,quantity);

        if(areInventoryView) {
            listAdapter.setDataCategory(inventory.getCategories());
            listAdapter.setDataItem(inventory.getItems());
            listAdapter.setDataItemQuantity(inventory.getItemsQuantity());
            listAdapter.notifyDataSetChanged();
        }

        new Toast(this).makeText(this,"Item added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoButtonPressed(String category) {
        Log.d("YourIt", "Info button is clicked: " + category);
    }

}
