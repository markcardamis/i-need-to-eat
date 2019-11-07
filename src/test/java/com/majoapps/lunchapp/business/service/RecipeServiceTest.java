package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.majoapps.lunchapp.business.domain.Recipe;
import com.majoapps.lunchapp.business.domain.RecipeWrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
class RecipeServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RecipeClientService recipes;

    @Test
    void getRecipesUsingTestRestClient() throws Exception {
        String resourceUrl = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
        RecipeWrapper recipes = restTemplate.getForObject(resourceUrl, RecipeWrapper.class);
        assertNotNull(recipes);
    }

    @Test
    void getRecipesUsingRestClient() throws Exception {
        RecipeWrapper recipeList = recipes.get();
        List<Recipe> recipe = recipeList.getRecipes();
        for (Recipe recipeItem : recipe) {		
            System.out.println(recipeItem);
        }
        assertNotNull(recipe);
    }

}
