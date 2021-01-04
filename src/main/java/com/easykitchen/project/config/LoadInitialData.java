package com.easykitchen.project.config;

import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.model.Role;
import com.easykitchen.project.model.User;
import com.easykitchen.project.service.CategoryService;
import com.easykitchen.project.service.RecipeService;
import com.easykitchen.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class LoadInitialData implements ApplicationRunner {

    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoadInitialData(RecipeService recipeService, CategoryService categoryService, UserService userService, PasswordEncoder passwordEncoder) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Category veganCategory = createVeganCategory();
        final Category massGainCategory = createMassGainCategory();
        final Category lowCalCategory = createLowCaloriesCategory();
        createAdminWithCredentials("administrator", "administrator");
        createRecipe("Cake", 100.0, veganCategory);
        createRecipe("Tofu", 150.0, veganCategory);
        createRecipe("Vegan Burger", 190.0, veganCategory);
        createRecipe("Vegan Pasta", 120.0, veganCategory);
        createRecipe("Beef Steak With Fries And Salad", 190.0, massGainCategory);
        createRecipe("Salmon Burger", 150.0, massGainCategory);
        createRecipe("Baked Caprese Chicken", 140.0, massGainCategory);
        createRecipe("Shrimp Scampi", 140.0, massGainCategory);
        createRecipe("Creamy tomato courgetti", 140.0, lowCalCategory);
        createRecipe("Thai fried prawn & pineapple rice", 130.0, lowCalCategory);
        createRecipe("Spice-cured tuna tacos", 160.0, lowCalCategory);
        createRecipe("Spice-cured tuna tacos", 160.0, lowCalCategory);
        createRecipe("Baked piri-piri tilapia with crushed potatoes", 140.0, lowCalCategory);
    }

    private Category createVeganCategory() {
        final Category category = new Category();
        category.setName("Vegan");
        category.setDescription("Recipes are made for vegans");
        categoryService.persist(category);
        return category;
    }

    private Category createMassGainCategory() {
        final Category category = new Category();
        category.setName("Mass Gain");
        category.setDescription("Recipes are made for people who are gaining muscles");
        categoryService.persist(category);
        return category;
    }

    private Category createLowCaloriesCategory() {
        final Category category = new Category();
        category.setName("Low Calories");
        category.setDescription("Recipes are made for people who are loosing weight");
        categoryService.persist(category);
        return category;
    }

    private void createRecipe(String name, Double price, Category category) {
        final Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setPrice(price);
        recipe.setAmount(100);
        recipe.setDescription("...");
        recipe.setCategories(Collections.singletonList(category));
        recipeService.persist(recipe);
    }

    private void createAdminWithCredentials(String username, String password) {
        final User user = new User();
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setUsername(username);
        user.setPassword(password);
        user.encodePassword(passwordEncoder);
        user.setRole(Role.ADMIN);
        userService.createUser(user);
    }
}
