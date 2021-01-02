package com.easykitchen.project.dao;

import com.easykitchen.project.ProjectApplication;
import com.easykitchen.project.environment.Generator;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.Recipe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
@ComponentScan(basePackageClasses = ProjectApplication.class)
public class RecipeDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private RecipeDao sut;

    @Test
    public void findAllByCategoryReturnsRecipesInSpecifiedCategory() {
        final Category cat = generateCategory("testCategory");

        final List<Recipe> recipes = generateRecipes(cat);
        final List<Recipe> result = sut.findAll(cat);
        assertEquals(recipes.size(), result.size());
        recipes.sort(Comparator.comparing(Recipe::getName));
        result.sort(Comparator.comparing(Recipe::getName));
        for (int i = 0; i < recipes.size(); i++) {
            assertEquals(recipes.get(i).getId(), result.get(i).getId());
        }
    }

    private Category generateCategory(String name) {
        final Category cat = new Category();
        cat.setName(name);
        em.persist(cat);
        return cat;
    }

    private List<Recipe> generateRecipes(Category category) {
        final List<Recipe> inCategory = new ArrayList<>();
        final Category other = generateCategory("otherCategory");
        for (int i = 0; i < 10; i++) {
            final Recipe r = Generator.generateRecipe(1);
            r.setCategories(new ArrayList<>(Collections.singletonList(other)));
            if (Generator.randomBoolean()) {
                r.addCategory(category);
                inCategory.add(r);
            }
            em.persist(r);
        }
        return inCategory;
    }
}
