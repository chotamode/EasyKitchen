package com.easykitchen.project.service;

import com.easykitchen.project.dao.RecipeDao;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RecipeService {
    private final RecipeDao dao;

    @Autowired
    public RecipeService(RecipeDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Recipe> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Recipe> findAll(Category category) {
        return dao.findAll(category);
    }

    @Transactional(readOnly = true)
    public Recipe find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Recipe recipe) {
        dao.persist(recipe);
    }

    @Transactional
    public void update(Recipe recipe) {
        dao.update(recipe);
    }

    @Transactional
    public void remove(Recipe recipe) {
        Objects.requireNonNull(recipe);
        recipe.setRemoved(true);
        dao.update(recipe);
    }
}
