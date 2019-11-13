package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.LunchResponse;
import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.entity.Lunch;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.LunchRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class LunchService {

    private final LunchRepository lunchRepository;
    private final RestTemplateService restTemplateService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @Autowired
    public LunchService(LunchRepository lunchRepository, RestTemplateService restTemplateService, 
                RecipeService recipeService, IngredientService ingredientService) {
        this.lunchRepository = lunchRepository;
        this.restTemplateService = restTemplateService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    public LunchResponse get() throws Exception {
        //clear lunch table database entries so duplicates do not appear, use soft delete in future
        lunchRepository.deleteAll();

        saveRecipeListToDatabase(restTemplateService.getRecipes());
        saveIngredientsListToDatabase(restTemplateService.getIngredients());

        // create a lunchMap (HashMap) from the recipe and ingredient database tables
        Map<String, List<Ingredient>> lunchMap = createLunchMapWithGoodIngredients();
        
        // save the recipes which are valid options in the lunch table
        saveRecipesWithCompleteIngredientsToDatabase(lunchMap);

        // order the lunch options by Best Before
        List<Lunch> lunches = lunchRepository.findAllByOrderByBestBeforeAsc();        

        // send back the response using the LunchResponse model
        LunchResponse lunchResponse = new LunchResponse();
        lunchResponse.setRecipes(lunches);

        return lunchResponse;
    }

    private void saveRecipeListToDatabase(List<RecipeDto> recipeDtos) {
        // Iterate through the recipe list and save to the database
        recipeDtos.forEach(recipeService::saveRecipe);
    }

    private void saveIngredientsListToDatabase(List<IngredientDto> ingredientDtos) {
        // Iterate through the ingredient list and save to the database
        ingredientDtos.forEach(ingredientService::saveIngredient);
    }

    private Map<String, List<Ingredient>> createLunchMapWithGoodIngredients() {
        //create a lunch map from the database only containing recipes with good ingredients
        Map<String, List<Ingredient>> lunchMap = new HashMap<>();
        List<Ingredient> ingredients = ingredientService.findByUseByAfter(LocalDate.now());
        for (Ingredient ingredient : ingredients) {
            List<Recipe> recipes = recipeService.findByIngredient(ingredient.getTitle());
            recipes.forEach(recipe -> {
                List<Ingredient> list = lunchMap.get(recipe.getTitle());
                if (list == null)
                {
                    list = new ArrayList<>();
                    lunchMap.put(recipe.getTitle(), list);
                }
                list.add(ingredient);
            });
        }

        return lunchMap;
    }

    private void saveRecipesWithCompleteIngredientsToDatabase(Map<String, List<Ingredient>> lunchMap) {
        //run through the lunch HashMap and check we have all the ingredients
        for (String recipeTitle : lunchMap.keySet()) {
            List<Recipe> recipes = recipeService.findByTitle(recipeTitle);
            //check if we have all the ingredients in the recipe after removing Use By ingredients
            if (lunchMap.get(recipeTitle).size() == recipes.size()) {
                Lunch lunchEntity = new Lunch();
                lunchEntity.setTitle(recipeTitle);
                if (lunchEntity.getBestBefore() == null) {
                    lunchEntity.setBestBefore(lunchMap.get(recipeTitle).get(0).getBestBefore());
                }
                for (Ingredient ingredientResponse : lunchMap.get(recipeTitle)) {
                    if (lunchEntity.getBestBefore().isAfter(ingredientResponse.getBestBefore())) {
                        lunchEntity.setBestBefore(ingredientResponse.getBestBefore());
                    }
                }
                //save the recipe in the lunch table once it is an option
                lunchRepository.save(lunchEntity);
            }       
        }
    }

}
