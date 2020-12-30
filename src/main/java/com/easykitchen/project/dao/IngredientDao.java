package com.easykitchen.project.dao;

import com.easykitchen.project.model.RecipeIngredient;
import org.springframework.stereotype.Repository;

@Repository
public class IngredientDao extends BaseDao<RecipeIngredient> {
    public IngredientDao() {
        super(RecipeIngredient.class);
    }
}
