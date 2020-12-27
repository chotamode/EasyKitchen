package com.easykitchen.project.rest;
import com.easykitchen.project.exception.NotFoundException;
import com.easykitchen.project.exception.ValidationException;
import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.rest.util.RestUtils;
import com.easykitchen.project.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/recipes")
public class RecipeController {
    private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Recipe> getRecipes() {
        return recipeService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRecipe(@RequestBody Recipe recipe) {
        recipeService.persist(recipe);
        LOG.debug("Created recipe {}.", recipe);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", recipe.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Recipe getRecipe(@PathVariable Integer id) {
        final Recipe r = recipeService.find(id);
        if (r == null) {
            throw NotFoundException.create("Recipe", id);
        }
        return r;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable Integer id, @RequestBody Recipe recipe) {
        final Recipe original = getRecipe(id);
        if (!original.getId().equals(recipe.getId())) {
            throw new ValidationException("Recipe identifier in the data does not match the one in the request URL.");
        }
        recipeService.update(recipe);
        LOG.debug("Updated recipe {}.", recipe);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRecipe(@PathVariable Integer id) {
        final Recipe toRemove = recipeService.find(id);
        if (toRemove == null) {
            return;
        }
        recipeService.remove(toRemove);
        LOG.debug("Removed recipe {}.", toRemove);
    }
}

