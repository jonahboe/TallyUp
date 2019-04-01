package com.tallyup;

import com.tallyup.datacontainers.Category;
import com.tallyup.datacontainers.Inventory;
import com.tallyup.datacontainers.Item;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestInventory {
    @Test
    public void TestGetSetItem() {
        Inventory inventory = new Inventory();
        for (int i = 0; i < 1500; i++) {
            inventory.addItem("Item","Category #" + Integer.toString(i),1);
        }
        assertEquals(inventory.getCategoriesList().size(), 1500);
    }
}
