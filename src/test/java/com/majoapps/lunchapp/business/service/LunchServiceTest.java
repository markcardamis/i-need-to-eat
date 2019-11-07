package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("test")
class LunchServiceTest {

    // @Autowired
    // private RecipeRepository recipeRepository;

    // @Test
    // void getRecipesWithIngredients() throws Exception {
    //     Iterable<Recipe> recipes = recipeRepository.findAll();
    //     recipes.forEach(recipe -> {
    //         System.out.println(recipe);
    //     });
    // }

    // @Test
    // void getRecipesWithGoodIngredients() throws Exception {
    //     // List<RecipeDto> recipe = recipeClientService.getRecipesRestTemplate();
    //     // for (RecipeDto recipeItem : recipe) {		
    //     //     System.out.println(recipeItem);
    //     // }
    //     // assertNotNull(recipe);
    // }

    // @Test
    // void filterRecipesByIngredientDate() throws Exception {

    // }

}
