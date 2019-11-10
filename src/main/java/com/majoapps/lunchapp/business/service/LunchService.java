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

    public LunchResponse get() {
        try { //fetch the recipes and ingredients then save to database
            List<RecipeDto> recipeDtos = restTemplateService.getRecipes();
            recipeDtos.forEach(recipeDto -> {
                recipeService.saveRecipe(recipeDto);
            });
            List<IngredientDto> ingredientDtos = restTemplateService.getIngredients();
            ingredientDtos.forEach(ingredientDto -> {
                ingredientService.saveIngredient(ingredientDto);
            });
        } catch (Exception e) {
            System.out.println("error: " + e.getLocalizedMessage());
        } finally {
            System.out.println("Fetched remote json");
        }
        
        //create a lunch map only containing recipes with good ingredients
        Map<String, List<Ingredient>> lunchMap = new HashMap<String, List<Ingredient>>();
        List<Ingredient> ingredients = ingredientService.findByUseByAfter(LocalDate.now());
        for (Ingredient ingredient : ingredients) {
            List<Recipe> recipes = recipeService.findByIngredient(ingredient.getTitle());
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

        //run through the lunch HashMap and save the recipes where we have all the ingredients
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
                lunchRepository.save(lunchEntity);
            }       
        }

        List<Lunch> lunches = lunchRepository.findAllByOrderByBestBeforeAsc();        

        LunchResponse lunchResponse = new LunchResponse();
        lunchResponse.setRecipes(lunches);

        return lunchResponse;
    }

}
