package com.easykitchen.project.service;

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

    @Autowired
    public CategoryService(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    @Transactional
    public List<Recipe> getAllRecipesByCategory(Category category) {
        Objects.requireNonNull(category);
        return recipeDao.findAll(category);
    }
}
