package com.easykitchen.project.dao;

import com.easykitchen.project.model.Ingredient;

public class IngredientDao extends BaseDao<Ingredient> {
    public IngredientDao() {
        super(Ingredient.class);
    }
}
