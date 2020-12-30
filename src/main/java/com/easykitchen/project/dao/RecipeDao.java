package com.easykitchen.project.dao;

import com.easykitchen.project.model.RecipeIngredient;
import org.springframework.stereotype.Repository;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;

import java.util.List;
import java.util.Objects;

@Repository
public class RecipeDao extends BaseDao<Recipe> {

    public RecipeDao() {
        super(Recipe.class);
    }

    @Override
    public List<Recipe> findAll() {
        return em.createQuery("SELECT r FROM Recipe r WHERE NOT r.available", Recipe.class).getResultList();
    }

    public List<Recipe> findAll(Category category) {
        Objects.requireNonNull(category);
        return em.createNamedQuery("Recipe.findByCategory", Recipe.class).setParameter("category", category)
                .getResultList();
    }

    public void addOneProduct(RecipeIngredient recipeIngredient, Recipe recipe) {
        Objects.requireNonNull(recipeIngredient);
        Objects.requireNonNull(recipe);

        List<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();

        if (recipeIngredients.contains(recipeIngredient)) {
            return;
        }

        recipeIngredients.add(recipeIngredient);
        recipe.setRecipeIngredients(recipeIngredients);
        persist(recipe);
    }

}
