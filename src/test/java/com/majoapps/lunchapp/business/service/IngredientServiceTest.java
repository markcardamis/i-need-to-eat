package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
class IngredientServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IngredientClientService ingredientClientService;

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
    void getIngredientsUsingRestClient() throws Exception {
        List<IngredientDto> ingredient = ingredientClientService.getIngredientsRestTemplate();
        for (IngredientDto ingredientItem : ingredient) {		
            System.out.println(ingredientItem);
        }
        assertNotNull(ingredient);
    }

    @Test
    void getIngredientsFromDatabase() throws Exception {
        Iterable<Ingredient> ingredients = ingredientClientService.ingredientRepository.findAll();
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

}
