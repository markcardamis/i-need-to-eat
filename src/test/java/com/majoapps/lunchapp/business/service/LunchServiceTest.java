package com.majoapps.lunchapp.business.service;

import static org.junit.Assert.assertNotNull;

import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.entity.Lunch;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import com.majoapps.lunchapp.data.repository.LunchRepository;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private LunchRepository lunchRepository;

    @Test
    void getRecipes() throws Exception {
        Iterable<Recipe> recipes = recipeRepository.findAll();
        recipes.forEach(recipe -> {
            System.out.println(recipe);
        });
        assertNotNull(recipes);
    }

    @Test
    void getIngredients() throws Exception {
        Iterable<Ingredient> ingredients = ingredientRepository.findAll();
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
        assertNotNull(ingredients);
    }

    @Test
    void getIngredientsUseByAfter() throws Exception {
        Iterable<Ingredient> ingredients = ingredientRepository.findByUseByAfter(LocalDate.now());
        ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });
        assertNotNull(ingredients);
    }

    @Test
    void getRecipesWithGoodIngredientsByDate() throws Exception {
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

        for (String recipeTitle : lunchMap.keySet()) {
            List<Recipe> recipes = recipeRepository.findByTitle(recipeTitle);
            if (lunchMap.get(recipeTitle).size() == recipes.size()) {
                Lunch lunchEntity = new Lunch();
                lunchEntity.setTitle(recipeTitle);
                    if (lunchEntity.getBestBefore() == null) {
                        lunchEntity.setBestBefore(lunchMap.get(recipeTitle).get(0).getBestBefore());
                    }
                    for (Ingredient ingredientResponse : lunchMap.get(recipeTitle)) {
                        if (lunchEntity.getBestBefore().isAfter(ingredientResponse.getBestBefore())){
                            lunchEntity.setBestBefore(ingredientResponse.getBestBefore());
                        }
                    }
                    lunchRepository.save(lunchEntity);
            }       
        }

        Iterable<Lunch> lunches = lunchRepository.findAllByOrderByBestBeforeAsc();

        System.out.println("Ascending");
        lunches.forEach(lunch -> {
            System.out.println(lunch);
        });

        lunches = lunchRepository.findAllByOrderByBestBeforeDesc();
        System.out.println("Descending");
        lunches.forEach(lunch -> {
            System.out.println(lunch);
        });

        assertNotNull(lunches);
    }

    @Test
    void get() {
    }

    @Test
    void filterRecipesByIngredientDate() throws Exception {

    }

}
