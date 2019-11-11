package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void getRecipesFromDatabase() throws Exception {
        Iterable<Recipe> recipes = recipeRepository.findAll();
        recipes.forEach(recipe -> {
            System.out.println(recipe);
        });
        assertNotNull(recipes);
    }

    @Test
    void getRecipesFromDatabaseViaService() throws Exception {
        Iterable<Recipe> recipes = recipeService.getRecipeRepository().findAll();
        recipes.forEach(recipe -> {
            System.out.println(recipe);
        });
        assertNotNull(recipes);
    }

    @Test
    void saveRecipe() {
    }

    @Test
    void findByTitle() {
    }

    @Test
    void findByIngredient() {
    }
}
