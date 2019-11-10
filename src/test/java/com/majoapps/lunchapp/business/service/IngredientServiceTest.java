package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.repository.IngredientRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
class IngredientServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void getIngredientsUsingTestRestClient() throws Exception {
        String fooResourceUrl = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
        IngredientDtoWrapper ingredients = restTemplate
            .getForObject(fooResourceUrl, IngredientDtoWrapper.class);
        List<IngredientDto> ingredient = ingredients.getIngredients();
        assertNotNull(ingredient);
    }

    @Test
    void saveIngredient() throws Exception {
        Ingredient ingredient = ingredientService.saveIngredient(null);
        assertNotNull(ingredient);
    }


    @Test
    void getIngredientsFromDatabase() throws Exception {
        Iterable<Ingredient> ingredients = ingredientService.getIngredientRepository().findAll();
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
    }

    @Test
    void getIngredientsByOrderByBestBeforeAsc() throws Exception {
        Iterable<Ingredient> ingredients = ingredientRepository.findAllByOrderByBestBeforeAsc();
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
    }

    @Test
    void saveIngredient1() {
    }

    @Test
    void findByUseByAfter() {
    }
}
