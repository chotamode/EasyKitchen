package com.easykitchen.project.rest;

import com.easykitchen.project.environment.Generator;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.service.CategoryService;
import com.easykitchen.project.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerModifyTest extends BaseControllerTestRunner {
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
    public void addRecipeToCategoryAddsRecipeToSpecifiedCategory() throws Exception {
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final Recipe re = Generator.generateRecipe();
        re.setId(Generator.randomInt());
        mockMvc.perform(post("/rest/categories/" + category.getId() + "/recipes").content(toJson(re)).contentType(
                MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNoContent());
        final ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(categoryServiceMock).addRecipe(eq(category), captor.capture());
        assertEquals(re.getId(), captor.getValue().getId());
    }

    @Test
    public void removeRecipeRemovesRecipeFromCategory() throws Exception {
        final Category category = new Category();
        category.setName("test");
        category.setId(Generator.randomInt());
        when(categoryServiceMock.find(any())).thenReturn(category);
        final Recipe recipe = Generator.generateRecipe();
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
