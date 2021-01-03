package com.easykitchen.project.rest;

import com.easykitchen.project.exception.NotFoundException;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.rest.util.RestUtils;
import com.easykitchen.project.service.CategoryService;
import com.easykitchen.project.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/categories")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService service;
    private final RecipeService recipeService;

    public CategoryController(CategoryService service,RecipeService recipeService) {
        this.service = service;
        this.recipeService = recipeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategories() {
        return service.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCategory(@RequestBody Category category) {
        service.persist(category);
        LOG.debug("Created category {}.", category);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", category.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Category getById(@PathVariable Integer id) {
        final Category category = service.find(id);
        if (category == null) {
            throw NotFoundException.create("Category", id);
        }
        return category;
    }

    @GetMapping(value = "/{id}/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Recipe> getProductsByCategory(@PathVariable Integer id) {
        return recipeService.findAll(getById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}/recipes/{recipeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addProductToCategory(@PathVariable Integer id, @PathVariable Integer recipeId) {
        final Category category = getById(id);
        final Recipe recipe = recipeService.find(recipeId);
        service.addRecipe(category, recipe);
        LOG.debug("Product {} added into category {}.", recipe, category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{categoryId}/recipes/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProductFromCategory(@PathVariable Integer categoryId,
                                          @PathVariable Integer recipeId) {
        final Category category = getById(categoryId);
        final Recipe toRemove = recipeService.find(recipeId);
        if (toRemove == null) {
            throw NotFoundException.create("Product", recipeId);
        }
        service.removeRecipe(category, toRemove);
        LOG.debug("Product {} removed from category {}.", toRemove, category);
    }
}
