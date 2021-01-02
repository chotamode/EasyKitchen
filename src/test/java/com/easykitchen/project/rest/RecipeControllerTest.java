package com.easykitchen.project.rest;


import com.easykitchen.project.environment.Generator;
import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.rest.handler.ErrorInfo;
import com.easykitchen.project.service.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeControllerTest extends BaseControllerTestRunner {

    @Mock
    private RecipeService recipeServiceMock;

    @InjectMocks
    private RecipeController sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(sut);
    }

    @Test
    public void getAllReturnsAllRecipes() throws Exception {
        final List<Recipe> recipes = IntStream.range(0, 5).mapToObj(i -> Generator.generateRecipe(1)).collect(
                Collectors.toList());
        when(recipeServiceMock.findAll()).thenReturn(recipes);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/recipes")).andReturn();
        final List<Recipe> result = readValue(mvcResult, new TypeReference<List<Recipe>>() {
        });
        assertNotNull(result);
        assertEquals(recipes.size(), result.size());
        for (int i = 0; i < recipes.size(); i++) {
            assertEquals(recipes.get(i).getName(), result.get(i).getName());
            assertEquals(recipes.get(i).getAmount(), result.get(i).getAmount());
        }
    }

    @Test
    public void getByIdReturnsRecipeWithMatchingId() throws Exception {
        final Recipe recipe = Generator.generateRecipe(1);
        recipe.setId(123);
        when(recipeServiceMock.find(recipe.getId())).thenReturn(recipe);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/recipes/" + recipe.getId())).andReturn();
        final Recipe result = readValue(mvcResult, Recipe.class);
        assertNotNull(result);
        assertEquals(recipe.getId(), result.getId());
        assertEquals(recipe.getName(), result.getName());
        assertEquals(recipe.getAmount(), result.getAmount());
    }

    @Test
    public void getByIdThrowsNotFoundForUnknownId() throws Exception {
        final int id = 123;
        final MvcResult mvcResult = mockMvc.perform(get("/rest/recipes/" + id)).andExpect(status().isNotFound())
                .andReturn();
        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("Recipe identified by "));
        assertThat(result.getMessage(), containsString(Integer.toString(id)));
    }

    @Test
    public void removeRemovesRecipeUsingService() throws Exception {
        final Recipe recipe = Generator.generateRecipe(1);
        recipe.setId(123);
        when(recipeServiceMock.find(recipe.getId())).thenReturn(recipe);
        mockMvc.perform(delete("/rest/recipes/" + recipe.getId())).andExpect(status().isNoContent());
        verify(recipeServiceMock).remove(recipe);
    }

    @Test
    public void removeDoesNothingWhenRecipeDoesNotExist() throws Exception {
        mockMvc.perform(delete("/rest/recipes/123")).andExpect(status().isNoContent());
        verify(recipeServiceMock, never()).remove(any());
    }
}