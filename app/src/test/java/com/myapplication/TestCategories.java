package com.myapplication;

import com.myapplication.datacontainers.Category;
import com.myapplication.datacontainers.Item;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestCategories {
    @Test
    public void TestGetSetItem() {
        Category category = new Category("Fruit");
        Item item = new Item("Apple", 2,3);
        category.addItem(item);
        assertEquals(category.getItems().get(0), item);
    }
}
