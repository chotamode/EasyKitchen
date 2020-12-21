package com.easykitchen.project.rest;

import com.easykitchen.project.environment.Generator;
import com.easykitchen.project.config.SecurityConfig;
import com.easykitchen.project.environment.TestSecurityConfig;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.model.Role;
import com.easykitchen.project.model.User;
import com.easykitchen.project.service.CategoryService;
import com.easykitchen.project.service.RecipeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.easykitchen.project.environment.Enviroment;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(
        classes = {TestSecurityConfig.class, CategoryControllerSecurityTest.TestConfig.class, SecurityConfig.class})
public class CategoryControllerSecurityTest extends BaseControllerTestRunner {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RecipeService recipeService;

    private User user;

    @Before
    public void setUp() {
        this.objectMapper=Enviroment.getObjectMapper();
        this.user = Generator.generateUser();
    }

    @After
    public void tearDown() {
        Enviroment.clearSecurityContext();
        Mockito.reset(categoryService, recipeService);
    }

    @Configuration
    @TestConfiguration
    public static class TestConfig {

        @Mock
        private CategoryService categoryService;
        @Mock
        private RecipeService recipeService;
        @InjectMocks
        private CategoryController categoryController;

        public TestConfig() {
            MockitoAnnotations.initMocks(this);
        }

        @Bean
        CategoryService categoryService() {
            return categoryService;
        }

        @Bean
        RecipeService recipetService() {
            return recipeService;
        }

        @Bean
        public CategoryController categoryController() {
            return categoryController;
        }
    }

    @WithAnonymousUser
    @Test
    public void addCategoryThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(
                post("/rest/categories").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
        verify(categoryService, never()).persist(any());
    }

    @WithMockUser
    @Test
    public void addCategoryThrowsForbiddenForRegularUser() throws Exception {
        Enviroment.setCurrentUser(user);
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(
                post("/rest/categories").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
        verify(categoryService, never()).persist(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void addCategoryWorksForAdminUser() throws Exception {
        user.setRole(Role.ADMIN);
        Enviroment.setCurrentUser(user);
        final Category toCreate = new Category();
        toCreate.setName("New Category");

        mockMvc.perform(
                post("/rest/categories").content(toJson(toCreate))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @WithAnonymousUser
    @Test
    public void addRecipetToCategoryThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Recipe r = Generator.generateRecipe();
        final int catId = 1;
        mockMvc.perform(post("/rest/categories/" + catId + "/recipes").content(toJson(r))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
        verify(categoryService, never()).addRecipe(any(), any());
    }

    @WithMockUser
    @Test
    public void addRecipeToCategoryIsForbiddenForRegularUser() throws Exception {
        Enviroment.setCurrentUser(user);
        final Recipe r = Generator.generateRecipe();
        final int catId = 1;
        mockMvc.perform(post("/rest/categories/" + catId + "/recipes").content(toJson(r))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
        verify(categoryService, never()).addRecipe(any(), any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void addRecipeToCategoryWorksForAdminUser() throws Exception {
        user.setRole(Role.ADMIN);
        Enviroment.setCurrentUser(user);
        final Recipe r = Generator.generateRecipe();
        r.setId(2);
        final Category c = new Category();
        final int catId = 1;
        when(categoryService.find(catId)).thenReturn(c);
        mockMvc.perform(post("/rest/categories/" + catId + "/recipes").content(toJson(r))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
        final ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(categoryService).addRecipe(eq(c), captor.capture());
        assertEquals(r.getId(), captor.getValue().getId());
    }

    @WithAnonymousUser
    @Test
    public void removeRecipeFromCategoryThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final int recipeId = 2;
        final int catId = 1;
        mockMvc.perform(delete("/rest/categories/" + catId + "/recipes/" + recipeId))
                .andExpect(status().isUnauthorized());
        verify(categoryService, never()).removeRecipe(any(), any());
    }

    @WithMockUser
    @Test
    public void removeRecipeFromCategoryIsForbiddenForRegularUser() throws Exception {
        Enviroment.setCurrentUser(user);
        final int recipeId = 2;
        final int catId = 1;
        mockMvc.perform(delete("/rest/categories/" + catId + "/recipes/" + recipeId))
                .andExpect(status().isForbidden());
        verify(categoryService, never()).removeRecipe(any(), any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void removeRecipeFromCategoryWorksForAdminUser() throws Exception {
        user.setRole(Role.ADMIN);
        Enviroment.setCurrentUser(user);
        final Recipe r = Generator.generateRecipe();
        final int recipeId = 2;
        final Category c = new Category();
        final int catId = 1;
        when(categoryService.find(catId)).thenReturn(c);
        when(recipeService.find(recipeId)).thenReturn(r);
        mockMvc.perform(delete("/rest/categories/" + catId + "/recipes/" + recipeId))
                .andExpect(status().isNoContent());
        verify(categoryService).removeRecipe(c, r);
    }
}
