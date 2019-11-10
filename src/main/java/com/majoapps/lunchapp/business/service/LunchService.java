package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.LunchResponse;
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
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class LunchService {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    LunchRepository lunchRepository;

    public LunchResponse get() {
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
            if (lunchMap.get(recipeTitle).size() == recipes.get(0).getIngredientCount()) {
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

        //convert Iterable to List
        List<Lunch> result = new ArrayList<Lunch>();
        for (Lunch str : lunches) {
            result.add(str);
        }

        LunchResponse lunchResponse = new LunchResponse();
        lunchResponse.setRecipes(result);

        return lunchResponse;
    }

}
