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
        RecipeIngredient recipeIngredient1 = Generator.generateIngredient(2, 4);
        RecipeIngredient recipeIngredient2 = Generator.generateIngredient(2, 4);
        recipe.setRecipeIngredients(new ArrayList<>(Arrays.asList(recipeIngredient1, recipeIngredient2)));
        entityManager.persist(recipe);

        CartItem cartItem = new CartItem();
        cartItem.setRecipe(recipe);

        cartService.addRecipe(cart, cartItem);

        final Cart result = entityManager.find(Cart.class, cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(recipe, result.getItems().get(0).getRecipe());
        assertEquals(2, result.getItems().get(0).getRecipe().getRecipeIngredients().size());
    }

    @Test
    public void testItemAddedToCartIsUnavailable() {
        final Recipe recipe = Generator.generateRecipe();
        RecipeIngredient recipeIngredient2 = Generator.generateIngredient(1, 1);
        recipe.addIngredient(recipeIngredient2);
        recipe.isAvailable();
        entityManager.persist(recipe);

        CartItem cartItem = new CartItem();
        cartItem.setRecipe(recipe);

        cartService.addRecipe(cart, cartItem);

        final Cart result = entityManager.find(Cart.class, cart.getId());
        assertEquals(0, result.getItems().get(0).getRecipe().getRecipeIngredients().get(0).getStorageIngredient().getAmount());
        assertEquals(1, result.getItems().size());
        assertEquals(false, result.getItems().get(0).getRecipe().isAvailable());
    }

    @Test
    public void testItemInsufficientIngredientAmount() {
        final Recipe recipe = Generator.generateRecipe();
        RecipeIngredient recipeIngredient2 = Generator.generateIngredient(1, 0);
        recipe.addIngredient(recipeIngredient2);
        recipe.isAvailable();
        entityManager.persist(recipe);

        CartItem cartItem = new CartItem();
        cartItem.setRecipe(recipe);

        assertThrows(InsufficientAmountException.class, () -> cartService.addRecipe(cart, cartItem));
    }
}