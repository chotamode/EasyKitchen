package com.easykitchen.project.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ORDER")
public class OrderItem extends Item {

    public OrderItem() {
    }

    public OrderItem(Recipe recipe) {
        setAmount(recipe.getAmount());
        setRecipe(recipe);
    }

    public OrderItem(Item other) {
        setAmount(other.getAmount());
        setRecipe(other.getRecipe());
    }
}
