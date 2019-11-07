package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
class IngredientServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IngredientClientService ingredients;

    @Test
    void getIngredientsUsingTestRestClient() throws Exception {
        String fooResourceUrl = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
        IngredientDtoWrapper ingredients = restTemplate.getForObject(fooResourceUrl, IngredientDtoWrapper.class);
        List<IngredientDto> ingredient = ingredients.getIngredients();
        assertNotNull(ingredient);
    }

    @Test
    void getIngredientsUsingRestClient() throws Exception {
        //IngredientDtoWrapper ingredientList = ingredients.get();
        List<IngredientDto> ingredient = ingredients.getIngredients();
        for (IngredientDto ingredientItem : ingredient) {		
            System.out.println(ingredientItem);
        }
        assertNotNull(ingredient);
    }

}
