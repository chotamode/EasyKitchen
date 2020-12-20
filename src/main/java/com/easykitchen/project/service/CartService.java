package com.easykitchen.project.service;

import com.easykitchen.project.dao.CartDao;
import com.easykitchen.project.dao.RecipeDao;
import com.easykitchen.project.exception.InsufficientAmountException;
import com.easykitchen.project.model.Cart;
import com.easykitchen.project.model.CartItem;
import com.easykitchen.project.model.Item;
import com.easykitchen.project.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class CartService {

    private final CartDao cartDao;

    private final RecipeDao recipeDao;

    @Autowired
    public CartService(CartDao cartDao, RecipeDao recipeDao) {
        this.cartDao = cartDao;
        this.recipeDao = recipeDao;
    }

    @Transactional
    public void addRecipe(Cart cart, CartItem cartItem) {
        Objects.requireNonNull(cart);
        Objects.requireNonNull(cartItem);
        Recipe recipe = cartItem.getRecipe();
        recipe.getRecipeIngredients()
                .forEach(ingredient -> {
                    if (ingredient.getAmount() > ingredient.getStorageIngredient().getAmount()) {
                        throw new InsufficientAmountException("The amount of ingredient " + ingredient
                                + " needed for the recipe " + recipe + " is insufficient");
                    }
                });
        if (cartItem.getRecipe().isAvailable()) {
            cart.addItem(cartItem);
            updateProductAvailabilityOnAdd(cartItem);
            cartDao.update(cart);
        }
    }

    @Transactional
    public void removeRecipe(Cart cart, CartItem cartItem) {
        Objects.requireNonNull(cart);
        Objects.requireNonNull(cartItem);
        cart.removeItem(cartItem);
        updateProductAvailabilityOnRemove(cartItem);
        cartDao.update(cart);
    }

    private void updateProductAvailabilityOnAdd(Item item) {
        final Recipe recipe = item.getRecipe();
        recipe.getRecipeIngredients()
                .forEach(ingredient -> {
                    if (ingredient.getAmount() > ingredient.getStorageIngredient().getAmount()) {
                        throw new InsufficientAmountException("The amount of ingredient " + ingredient
                                + " needed for the recipe " + recipe + " is insufficient");
                    }
                    ingredient.getStorageIngredient().setAmount(ingredient.getStorageIngredient().getAmount() - ingredient.getAmount());
                });
        recipe.isAvailable();
        recipeDao.update(recipe);
    }

    private void updateProductAvailabilityOnRemove(Item item) {
        final Recipe recipe = item.getRecipe();
        recipe.getRecipeIngredients()
                .forEach(ingredient -> {
                    ingredient.getStorageIngredient().setAmount(ingredient.getStorageIngredient().getAmount() + ingredient.getAmount());
                });
        recipe.isAvailable();
        recipeDao.update(recipe);
    }
}
