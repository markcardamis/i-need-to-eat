package com.majoapps.lunchapp.business.service;

import java.util.List;

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

    public Iterable<Recipe> get() throws Exception {
        return recipeRepository.findAll();
    }

    public List<RecipeDto> getRecipes() throws Exception {
        return restTemplate.getForObject(ROOT_URI, RecipeDtoWrapper.class).getRecipes();
    }

    public List<RecipeDto> saveRecipes() throws Exception {
        List<RecipeDto> recipes = getRecipes();
        if (recipes == null || recipes.size() == 0) {
            throw new ResourceNotFoundException("Cannot retrieve any recipes");
        } else {
            // iterate and save each object if it doesn't already exist
            recipes.forEach(recipe -> {
                Recipe recipeEntity = new Recipe();
                List<Recipe> recipeResponse = recipeRepository.findByTitle(recipe.getTitle());
                if (recipeResponse.size() == 0) {
                    //add new recipe as it doesn't exist
                    recipeEntity.setTitle(recipe.getTitle());
                    recipeEntity = recipeRepository.save(recipeEntity);
                } else {
                    recipeEntity = recipeResponse.get(0);
                }

                for (String ingredient : recipe.getIngredients()) {
                    Ingredient ingredientEntity = new Ingredient();
                    ingredientEntity.setTitle(ingredient);
                    ingredientEntity.setRecipe(recipeEntity);
                    ingredientRepository.save(ingredientEntity);
                }
            });
        }
        return recipes;
    }   

}
