package com.tallyup;

import com.tallyup.datacontainers.Category;
import com.tallyup.datacontainers.Item;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestCategories {
    @Test
    public void TestGetSetItem() {
        Category category = new Category("Fruit");
        Item item = new Item("Apple", 23);
        category.addItem(item);
        assertEquals(category.getItems().get(0), item);
    }
}
