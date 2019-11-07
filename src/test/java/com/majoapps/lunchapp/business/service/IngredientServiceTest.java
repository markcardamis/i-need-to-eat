package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.majoapps.lunchapp.business.domain.Ingredient;
import com.majoapps.lunchapp.business.domain.IngredientWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
class IngredientServiceTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IngredientClientService ingredients;

    @Test
    void getIngredientsUsingTestRestClient() throws Exception {
        String fooResourceUrl = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
        IngredientWrapper ingredients = restTemplate.getForObject(fooResourceUrl, IngredientWrapper.class);
        List<Ingredient> ingredient = ingredients.getIngredients();
        assertNotNull(ingredient);
    }

    @Test
    void getIngredientsUsingRestClient() throws Exception {
        IngredientWrapper ingredientList = ingredients.get();
        List<Ingredient> ingredient = ingredientList.getIngredients();
        for (Ingredient ingredientItem : ingredient) {		
            System.out.println(ingredientItem);
        }
        assertNotNull(ingredient);
    }
}
