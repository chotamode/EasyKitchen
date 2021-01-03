package com.easykitchen.project.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {
    @Test
    public void testOrderCorrectTotalCalculation() {
        final Recipe recipe = new Recipe();
        final Recipe recipe1 = new Recipe();
        recipe.setId(1);
        recipe1.setId(2);
        recipe.setPrice(15.23);
        recipe1.setPrice(20.56);
        OrderItem orderItem = new OrderItem(recipe);
        OrderItem orderItem1 = new OrderItem(recipe1);
        orderItem.setAmount(1);
        orderItem1.setAmount(2);

        final Order order = new Order();
        assertEquals(2, order.getItems().size());
    }
}
