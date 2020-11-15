package com.easykitchen.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {

    @Test
    public void testOrderCorrectTotalCalculation() {
        final Recipe recipe = new Recipe();
        final Recipe recipe1 = new Recipe();
        final Cart cart = new Cart();
        final CartItem cartItem = new CartItem();
        final CartItem cartItem1 = new CartItem();
        recipe.setId(1);
        recipe1.setId(2);
        recipe.setPrice(15.23);
        recipe1.setPrice(20.56);
        cartItem.setRecipe(recipe);
        cartItem1.setRecipe(recipe1);
        cart.addItem(cartItem);
        cart.addItem(cartItem1);

        final Order order = new Order(cart);
        assertEquals(2, order.getItems().size());
        assertTrue(35.79 == order.getTotal());
    }
}
