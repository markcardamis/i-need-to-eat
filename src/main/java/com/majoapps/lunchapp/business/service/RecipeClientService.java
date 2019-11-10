package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.domain.RecipeDtoWrapper;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import com.majoapps.lunchapp.exception.ResourceNotFoundException;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            // iterate and save each recipe as long as there is one ingredient
            recipes.forEach(recipe -> {
                Recipe recipeEntity = new Recipe();
                List<Recipe> recipeResponse = recipeRepository.findByTitle(recipe.getTitle());
                if (recipeResponse.isEmpty()) { //add new recipe as it doesn't exist
                    recipeEntity.setTitle(recipe.getTitle());
                } else {
                    recipeEntity = recipeResponse.get(0);
                }
                for (String ingredient : recipe.getIngredients()) {
                    List<Recipe> ingredientResponse = recipeRepository
                        .findByTitleAndIngredient(recipeEntity.getTitle(), ingredient);
                    if (ingredientResponse.isEmpty()) { //add new ingredient to recipe
                        Recipe recipeIngredientEntity = new Recipe();
                        recipeIngredientEntity.setTitle(recipeEntity.getTitle());
                        recipeIngredientEntity.setIngredient(ingredient);
                        recipeIngredientEntity = recipeRepository.save(recipeIngredientEntity);
                    } 
                }
            });
        }
        return recipes;
    }   

}
