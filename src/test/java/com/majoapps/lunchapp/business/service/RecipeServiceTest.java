package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.domain.RecipeDtoWrapper;
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
    private RecipeClientService recipeClientService;

    @Test
    void getRecipesUsingTestRestClient() throws Exception {
        String resourceUrl = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
        RecipeDtoWrapper recipes = restTemplate.getForObject(resourceUrl, RecipeDtoWrapper.class);
        assertNotNull(recipes);
    }

    @Test
    void getRecipesUsingRestClient() throws Exception {
        List<RecipeDto> recipe = recipeClientService.getRecipesRestTemplate();
        for (RecipeDto recipeItem : recipe) {		
            System.out.println(recipeItem);
        }
        assertNotNull(recipe);
    }

}
