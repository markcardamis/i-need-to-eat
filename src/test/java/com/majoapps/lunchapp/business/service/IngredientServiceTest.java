package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.majoapps.lunchapp.business.domain.IngredientList;
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
}
