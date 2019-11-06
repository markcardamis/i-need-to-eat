package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import com.majoapps.lunchapp.business.domain.Ingredient;
import com.majoapps.lunchapp.business.domain.IngredientList;
import com.majoapps.lunchapp.business.domain.IngredientWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class IngredientServiceTest {

    @Test
    void getIngredients() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
        IngredientList ingredients = restTemplate.getForObject(fooResourceUrl, IngredientList.class);
        assertNotNull(ingredients);
    }

    @Test
    void getIngredients1() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
        IngredientWrapper ingredients = restTemplate.getForObject(fooResourceUrl, IngredientWrapper.class);
        List<Ingredient> ingredient = ingredients.getIngredients();
        assertNotNull(ingredient);
    }
}
