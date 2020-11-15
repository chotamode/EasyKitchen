package com.easykitchen.project.service;

import com.easykitchen.project.environment.Generator;
import com.easykitchen.project.exception.NonExistingCustomer;
import com.easykitchen.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    private Cart cart;

    @BeforeEach
    public void setUp() {
        final User owner = Generator.generateUser();
        this.cart = new Cart();
        owner.setCart(cart);
        cart.setOwner(owner);
        entityManager.persist(owner);
    }

    @Test
    public void testSuccessfullyCreatedOrder() {
        prepareCartForOrder();

        final Cart cartUpdated = entityManager.find(Cart.class, cart.getId());
        final Order order = orderService.createOrder(cartUpdated);
        final Order orderResult = entityManager.find(Order.class, order.getId());
        final Cart cartResult = entityManager.find(Cart.class, cart.getId());

        assertEquals(0, cartResult.getItems().size());
        assertEquals(order, orderResult);
    }

    @Test
    public void testUnsuccessfullyCreatedOrderCartWithoutOwner() {
        cart.setOwner(null);
        prepareCartForOrder();

        final Cart cartUpdated = entityManager.find(Cart.class, cart.getId());

        assertThrows(NonExistingCustomer.class, () -> orderService.createOrder(cartUpdated));
    }

    private void prepareCartForOrder() {
        final Recipe recipe = Generator.generateRecipe();
        Ingredient ingredient1 = Generator.generateIngredient(2);
        Ingredient ingredient2 = Generator.generateIngredient(2);
        recipe.setAvailable(true);
        recipe.setIngredients(new ArrayList<>(Arrays.asList(ingredient1, ingredient2)));
        entityManager.persist(recipe);
        CartItem cartItem = new CartItem();
        cartItem.setRecipe(recipe);
        cartService.addRecipe(cart, cartItem);
    }
}