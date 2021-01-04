package com.easykitchen.project.rest;


import com.easykitchen.project.environment.Generator;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.rest.handler.ErrorInfo;
import com.easykitchen.project.service.CategoryService;
import com.easykitchen.project.service.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest extends BaseControllerTestRunner {

    @Mock
    private CategoryService categoryServiceMock;

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private CategoryController sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(sut);
    }

    @Test
    public void getAllReturnsCategoriesReadByCategoryService() throws Exception {
        final List<Category> categories = IntStream.range(0, 5).mapToObj(i -> {
            final Category cat = new Category();
            cat.setName("Category" + i);
            cat.setId(Generator.randomInt());
            return cat;
        }).collect(Collectors.toList());
        when(categoryServiceMock.findAll()).thenReturn(categories);

        final MvcResult mvcResult = mockMvc.perform(get("/rest/categories")).andReturn();
        final List<Category> result = readValue(mvcResult, new TypeReference<List<Category>>() {
        });
        assertEquals(categories.size(), result.size());
        verify(categoryServiceMock).findAll();
    }

    @Test
    public void createCategoryCreatesCategoryUsingService() throws Exception {
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(post("/rest/categories").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        final ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryServiceMock).persist(captor.capture());
        assertEquals(toCreate.getName(), captor.getValue().getName());
    }

    @Test
    public void createCategoryReturnsResponseWithLocationHeader() throws Exception {
        final Category toCreate = new Category();
        toCreate.setName("New Category");
        toCreate.setId(Generator.randomInt());

        final MvcResult mvcResult = mockMvc
                .perform(post("/rest/categories").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated()).andReturn();
        verifyLocationEquals("/rest/categories/" + toCreate.getId(), mvcResult);
    }

    @Test
    public void getByIdReturnsMatchingCategory() throws Exception {
        final Category category = new Category();
        category.setId(Generator.randomInt());
        category.setName("category");
        when(categoryServiceMock.find(category.getId())).thenReturn(category);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/categories/" + category.getId())).andReturn();

        final Category result = readValue(mvcResult, Category.class);
        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
    }

    @Test
    public void getByIdThrowsNotFoundForUnknownCategoryId() throws Exception {
        final int id = 123;
        final MvcResult mvcResult = mockMvc.perform(get("/rest/categories/" + id)).andExpect(status().isNotFound())
                .andReturn();
        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("Category identified by "));
        assertThat(result.getMessage(), containsString(Integer.toString(id)));
    }

    @Test
    public void getRecipesByCategoryReturnsRecipesForCategory() throws Exception {
        final List<Recipe> recipes = Arrays.asList(Generator.generateRecipe(1), Generator.generateRecipe(1));
        when(recipeService.findAll(any())).thenReturn(recipes);
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/categories/" + category.getId() + "/recipes")).andReturn();
        final List<Recipe> result = readValue(mvcResult, new TypeReference<List<Recipe>>() {
        });
        assertNotNull(result);
        assertEquals(recipes.size(), result.size());
        verify(categoryServiceMock).find(category.getId());
        verify(recipeService).findAll(category);
    }

    @Test
    public void getRecipesByCategoryThrowsNotFoundForUnknownCategoryId() throws Exception {
        final int id = 123;
        mockMvc.perform(get("/rest/categories/" + id + "/recipes")).andExpect(status().isNotFound());
        verify(categoryServiceMock).find(id);
        verify(recipeService, never()).findAll(any());
    }

    @Test
    public void removeRecipeRemovesRecipeFromCategory() throws Exception {
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final Recipe recipe = Generator.generateRecipe(1);
        recipe.setId(Generator.randomInt());
        recipe.addCategory(category);
        when(recipeService.find(any())).thenReturn(recipe);
        mockMvc.perform(delete("/rest/categories/" + category.getId() + "/recipes/" + recipe.getId()))
                .andExpect(status().isNoContent());
        verify(categoryServiceMock).removeRecipe(category, recipe);
    }

    @Test
    public void removeRecipeThrowsNotFoundForUnknownRecipeId() throws Exception {
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final int unknownId = 123;
        mockMvc.perform(delete("/rest/categories/" + category.getId() + "/recipes/" + unknownId))
                .andExpect(status().isNotFound());
        verify(categoryServiceMock).find(category.getId());
        verify(categoryServiceMock, never()).removeRecipe(any(), any());
    }
}
