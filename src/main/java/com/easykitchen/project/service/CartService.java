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
        if (!cartItem.getRecipe().isRemoved() && (cartItem.getRecipe().getAmount() > 0)) {
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
        recipe.setAmount(recipe.getAmount() - 1);
        recipe.setRemoved(recipe.getAmount() <= 0);
        recipeDao.update(recipe);
    }

    private void updateProductAvailabilityOnRemove(Item item) {
        final Recipe recipe = item.getRecipe();
        recipe.setAmount(recipe.getAmount() + 1);
        recipe.setRemoved(recipe.getAmount() <= 0);
        recipeDao.update(recipe);
    }
}
