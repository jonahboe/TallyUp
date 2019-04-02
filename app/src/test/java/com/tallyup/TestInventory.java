package com.tallyup;

import com.tallyup.datacontainers.Inventory;
import com.tallyup.datacontainers.Item;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestInventory {
    @Test
    public void AddCrapTonOfItems() {
        Inventory inventory = new Inventory();
        for (int i = 0; i < 1500; i++) {
            inventory.addItem("Item","Category #" + Integer.toString(i),1);
        }
        assertEquals(inventory.getCategoriesList().size(), 1500);
    }

    @Test
    public void QuantityOverlap() {
        Inventory inventory = new Inventory();
        for (int i = 0; i < 1500; i++) {
            inventory.addItem("Item","Category",1);
        }
        assertEquals(inventory.getItem("Category", "Item").getQuantity(), 1500);
    }

    @Test
    public void MoveItems() {
        Inventory inventory = new Inventory();
        Inventory sold = new Inventory();
        inventory.addItem("Item","Category",50);
        Item i = inventory.getItem("Category", "Item");
        sold.addItem("Item", "Category", 10);
        i.setQuantity(i.getQuantity() - 10);
        sold.sortItems();

        assertEquals(inventory.getItem("Category", "Item").getQuantity(), 40);
        assertEquals(sold.getItem("Category", "Item").getQuantity(), 10);
    }

    @Test
    public void TestSorting() {
        Inventory inventory = new Inventory();
        inventory.addItem("B", "Category", 15);
        inventory.addItem("A","Category",10);
        inventory.sortItems();

        assertEquals(inventory.getCategory("Category").getItems().get(0).getName(), "A");
    }
}
