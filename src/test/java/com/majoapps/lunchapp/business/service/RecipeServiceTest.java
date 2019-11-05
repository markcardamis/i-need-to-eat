package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.majoapps.lunchapp.business.domain.RecipeList;
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
}
