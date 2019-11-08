package com.majoapps.lunchapp.business.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("test")
class LunchServiceTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void getRecipes() throws Exception {
        Iterable<Recipe> recipes = recipeRepository.findAll();
        recipes.forEach(recipe -> {
            System.out.println(recipe);
        });
    }

    @Test
    void getIngredients() throws Exception {
        Iterable<Ingredient> ingredients = ingredientRepository.findAll();
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
    }

    @Test
    void getIngredients1() throws Exception {
        Iterable<Ingredient> ingredients = ingredientRepository.findAll();
        List<Recipe> recipeList = new ArrayList<>();
        ingredients.forEach(ingredient -> {
            recipeList.add(ingredient.getRecipe());
        });
        Integer occurrences = Collections.frequency(recipeList, "Fry-up");
        System.out.println(ingredients);
    }

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
