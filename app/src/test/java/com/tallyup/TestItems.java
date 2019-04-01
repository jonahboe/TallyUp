package com.tallyup;

import com.tallyup.datacontainers.Item;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestItems {
    @Test
    public void ItemNameWorks() {
        Item item = new Item("Apple", (float) 10.50, 10);
        assertEquals(item.getName(),"Apple");
    }
    @Test
    public void ItemQuantityWorks() {
        Item item = new Item("Apple", (float) 10.50, 10);
        assertEquals(item.getQuantity(), 10);
    }
}
