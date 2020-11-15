package com.easykitchen.project.service;

import com.easykitchen.project.environment.Generator;
import com.easykitchen.project.exception.InsufficientAmountException;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CartService cartService;

    private Cart cart;

    @BeforeEach
    public void setUp() {
        final User owner = Generator.generateUser();
        this.cart = new Cart();
        owner.setCart(cart);
        entityManager.persist(owner);
    }

    @Test
    public void testItemSuccessfullyAddedToCart() {
        final Recipe recipe = Generator.generateRecipe();
        recipe.setAvailable(true);
        Ingredient ingredient1 = Generator.generateIngredient(2);
        Ingredient ingredient2 = Generator.generateIngredient(2);
        recipe.setIngredients(new ArrayList<>(Arrays.asList(ingredient1, ingredient2)));
        entityManager.persist(recipe);

        CartItem cartItem = new CartItem();
        cartItem.setRecipe(recipe);

        cartService.addRecipe(cart, cartItem);

        final Cart result = entityManager.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(recipe, result.getItems().get(0).getRecipe());
        assertEquals(2, result.getItems().get(0).getRecipe().getIngredients().size());
    }

    @Test
    public void testItemAddedToCartIsUnavailable() {
        final Recipe recipe = Generator.generateRecipe();
        recipe.setAvailable(true);
        Ingredient ingredient2 = Generator.generateIngredient(1);
        recipe.setIngredients(new ArrayList<>(Collections.singletonList(ingredient2)));
        entityManager.persist(recipe);

        CartItem cartItem = new CartItem();
        cartItem.setRecipe(recipe);

        cartService.addRecipe(cart, cartItem);

        final Cart result = entityManager.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(false, result.getItems().get(0).getRecipe().isAvailable());
    }

    @Test
    public void testItemInsufficientIngredientAmount() {
        final Recipe recipe = Generator.generateRecipe();
        recipe.setAvailable(true);
        Ingredient ingredient2 = Generator.generateIngredient(0);
        recipe.setIngredients(new ArrayList<>(Collections.singletonList(ingredient2)));
        entityManager.persist(recipe);

        CartItem cartItem = new CartItem();
        cartItem.setRecipe(recipe);

        assertThrows(InsufficientAmountException.class, () -> cartService.addRecipe(cart, cartItem));
    }
}