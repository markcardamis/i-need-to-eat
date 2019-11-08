package com.majoapps.lunchapp.business.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.majoapps.lunchapp.business.domain.LunchOptions;
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
    void getIngredientsUseByAfter() throws Exception {
        Iterable<Ingredient> ingredients = ingredientRepository.findByUseByAfter(LocalDate.now());
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
    }


    @Test
    void getRecipesWithGoodIngredients() throws Exception {
        Map<String, List<Ingredient>> lunchMap = new HashMap<String, List<Ingredient>>();

        Iterable<Ingredient> ingredients = ingredientRepository.findByUseByAfter(LocalDate.now());
        for (Ingredient ingredient : ingredients) {
            Iterable<Recipe> recipes = recipeRepository.findByIngredient(ingredient.getTitle());
            recipes.forEach(recipe -> {
                List<Ingredient> list = lunchMap.get(recipe.getTitle());
                if (list == null)
                {
                    list = new ArrayList<Ingredient>();
                    lunchMap.put(recipe.getTitle(), list);
                }
                list.add(ingredient);
            });
        }

        List<LunchOptions> lunchOptions = new ArrayList<>();
        for (String recipeTitle : lunchMap.keySet()) {
            List<Recipe> recipes = recipeRepository.findByTitle(recipeTitle);
            if (lunchMap.get(recipeTitle).size() == recipes.get(0).getIngredientCount()) {
                LunchOptions lunchOption = new LunchOptions();
                lunchOption.setTitle(recipeTitle);
                    if (lunchOption.getBestBefore() == null) {
                        lunchOption.setBestBefore(lunchMap.get(recipeTitle).get(0).getBestBefore());
                    }
                    for (Ingredient ingredientResponse : lunchMap.get(recipeTitle)) {
                        if (lunchOption.getBestBefore().isAfter(ingredientResponse.getBestBefore())){
                            lunchOption.setBestBefore(ingredientResponse.getBestBefore());
                        }
                    }
                lunchOptions.add(lunchOption);
            }       
        }

        System.out.println(lunchOptions);

    }

    // @Test
    // void filterRecipesByIngredientDate() throws Exception {

    // }

}
