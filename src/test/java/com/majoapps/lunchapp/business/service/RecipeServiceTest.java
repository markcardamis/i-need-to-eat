package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.majoapps.lunchapp.business.domain.Recipe;
import com.majoapps.lunchapp.business.domain.RecipeList;
import com.majoapps.lunchapp.business.domain.RecipeWrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class RecipeServiceTest {

    @Test
    void getRecipes() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
        RecipeList recipes = restTemplate.getForObject(resourceUrl, RecipeList.class);
        assertNotNull(recipes);
    }

    @Test
    void getRecipes1() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
        RecipeWrapper recipes = restTemplate.getForObject(fooResourceUrl, RecipeWrapper.class);
        List<Recipe> recipe = recipes.getRecipes();
        assertNotNull(recipe);
    }

}
