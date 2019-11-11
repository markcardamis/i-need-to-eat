package com.majoapps.lunchapp.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("test")
class IngredientServiceTest {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void getIngredientsFromDatabase() throws Exception {
        Iterable<Ingredient> ingredients = ingredientService.getIngredientRepository().findAll();
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
        assertNotNull(ingredients);
    }

    @Test
    void getIngredientsByOrderByBestBeforeAsc() throws Exception {
        Iterable<Ingredient> ingredients = ingredientRepository.findAllByOrderByBestBeforeAsc();
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
        assertNotNull(ingredients);
    }

    @Test
    void saveIngredient() {
    }

    @Test
    void findByUseByAfter() {
    }
}
