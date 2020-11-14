package com.easykitchen.project.dao;

import com.easykitchen.project.model.Ingredient;
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

    public void addOneProduct(Ingredient ingredient, Recipe recipe) {
        Objects.requireNonNull(ingredient);
        Objects.requireNonNull(recipe);

        List<Ingredient> ingredients = recipe.getIngredients();

        if (ingredients.contains(ingredient)) {
            return;
        }

        ingredients.add(ingredient);
        recipe.setIngredients(ingredients);
        persist(recipe);
    }

}
