package com.easykitchen.project.dao;

import com.easykitchen.project.model.Ingredient;
import jdk.jfr.Registered;
import org.springframework.stereotype.Repository;

@Repository
public class IngredientDao extends BaseDao<Ingredient> {
    public IngredientDao() {
        super(Ingredient.class);
    }
}
