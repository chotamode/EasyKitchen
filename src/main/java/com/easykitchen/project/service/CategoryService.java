package com.easykitchen.project.service;

import com.easykitchen.project.dao.CategoryDao;
import com.easykitchen.project.dao.RecipeDao;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    private final RecipeDao recipeDao;
    private final CategoryDao dao;

    @Autowired
    public CategoryService(CategoryDao dao,RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
        this.dao = dao;
    }
    @Transactional
    public void persist(Category category) {
        Objects.requireNonNull(category);
        dao.persist(category);
    }
    @Transactional
    public void addRecipe(Category category, Recipe recipe) {
        Objects.requireNonNull(category);
        Objects.requireNonNull(recipe);
        recipe.addCategory(category);
        recipeDao.update(recipe);
    }
    @Transactional
    public void removeRecipe(Category category, Recipe recipe) {
        Objects.requireNonNull(category);
        Objects.requireNonNull(recipe);
        recipe.removeCategory(category);
        recipeDao.update(recipe);
    }
    @Transactional
    public Category find(Integer id) {
        return dao.find(id);
    }
    @Transactional
    public List<Category> findAll() {
        return dao.findAll();
    }

    @Transactional
    public List<Recipe> getAllRecipesByCategory(Category category) {
        Objects.requireNonNull(category);
        return recipeDao.findAll(category);
    }
}
