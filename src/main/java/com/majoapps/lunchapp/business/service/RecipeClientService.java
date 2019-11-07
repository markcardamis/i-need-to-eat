package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.domain.RecipeDtoWrapper;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import com.majoapps.lunchapp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import lombok.Data;

@Data
@Service
public class RecipeClientService {

    final String ROOT_URI = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    public List<RecipeDto> getRecipesRestTemplate() throws Exception {
        return restTemplate.getForObject(ROOT_URI, RecipeDtoWrapper.class).getRecipes();
    }

    public List<RecipeDto> saveRecipes() throws Exception {
        List<RecipeDto> recipes = getRecipesRestTemplate();
        if (recipes == null || recipes.isEmpty()) {
            throw new ResourceNotFoundException("Cannot retrieve any recipes");
        } else {
            // iterate and save each object if it doesn't already exist
            recipes.forEach(recipe -> {
                Recipe recipeEntity = new Recipe();
                List<Recipe> recipeResponse = recipeRepository.findByTitle(recipe.getTitle());
                if (recipeResponse.isEmpty()) { //add new recipe as it doesn't exist
                    recipeEntity.setTitle(recipe.getTitle());
                    recipeEntity.setIngredientCount(recipe.getIngredients().size());
                    recipeEntity = recipeRepository.save(recipeEntity);
                } else {
                    recipeEntity = recipeResponse.get(0);
                }

                for (String ingredient : recipe.getIngredients()) {
                    List<Ingredient> ingredientResponse = ingredientRepository.findByTitle(ingredient);
                    Ingredient ingredientEntity = new Ingredient();
                    if (ingredientResponse.isEmpty()) { //add new ingredient as it doesn't exist 
                        ingredientEntity.setTitle(ingredient);
                        ingredientEntity.setRecipe(recipeEntity);
                        ingredientRepository.save(ingredientEntity);
                    } else for (Ingredient ingredientTemp : ingredientResponse) { //modify existing
                        //ingredientEntity.setId(ingredientTemp.getId());
                        ingredientEntity.setTitle(ingredientTemp.getTitle());
                        ingredientEntity.setRecipe(ingredientTemp.getRecipe());
                        ingredientRepository.save(ingredientEntity);
                    }
                }
            });
        }
        return recipes;
    }   

}
