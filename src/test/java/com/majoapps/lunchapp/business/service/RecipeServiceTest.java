package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;


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
