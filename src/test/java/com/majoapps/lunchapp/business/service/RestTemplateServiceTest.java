package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.RecipeDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
class RestTemplateServiceTest {

    @Autowired
    private RestTemplateService restTemplateService;

    @Test
    void getRecipes() throws Exception {
        List<RecipeDto> recipes = restTemplateService.getRecipes();
        assertNotNull(recipes);
    }

    @Test
    void getIngredients() throws Exception {
        List<IngredientDto> ingredient = restTemplateService.getIngredients();
        assertNotNull(ingredient);
    }
}