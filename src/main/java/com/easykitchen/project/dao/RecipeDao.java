package com.easykitchen.project.dao;

import org.springframework.stereotype.Repository;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Objects;

@Repository
public class RecipeDao extends BaseDao<Recipe> {

    public RecipeDao() {
        super(Recipe.class);
    }

    @Override
    public List<Recipe> findAll() {
        return em.createQuery("SELECT r FROM Recipe r WHERE NOT r.removed", Recipe.class).getResultList();
    }

    public List<Recipe> findAll(Category category) {
        Objects.requireNonNull(category);
        return em.createNamedQuery("Recipe.findByCategory", Recipe.class).setParameter("category", category)
                .getResultList();
    }

    public List<Recipe> findAllByAmount(Category category, Integer amount) {
        Objects.requireNonNull(category);
        return em.createNamedQuery("Recipe.findByCategory", Recipe.class).setParameter("category", category)
                .getResultList().subList(0, amount);
    }

}
