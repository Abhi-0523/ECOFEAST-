package com.ecofeast.model;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class FoodCategoryTest {

    @Test
    public void testGettersAndSetters() {
        FoodCategory cat = new FoodCategory();
        cat.setCategoryId(3);
        cat.setCategoryName("Beverages");
        cat.setDescription("Juices, sodas, and water");
        cat.setIconClass("fa-glass");
        cat.setActive(true);

        LocalDateTime created = LocalDateTime.now();
        cat.setCreatedAt(created);

        assertEquals(3, cat.getCategoryId());
        assertEquals("Beverages", cat.getCategoryName());
        assertEquals("Juices, sodas, and water", cat.getDescription());
        assertEquals("fa-glass", cat.getIconClass());
        assertTrue(cat.isActive());
        assertEquals(created, cat.getCreatedAt());
    }
}
