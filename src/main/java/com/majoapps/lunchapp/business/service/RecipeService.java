package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository){
        this.recipeRepository = recipeRepository;
    }

    public Recipe saveRecipe(RecipeDto recipe) {
        Recipe recipeEntity = new Recipe();
        for (String ingredient : recipe.getIngredients()) { //save a new record per ingredient
            List<Recipe> ingredientResponse = recipeRepository
                .findByTitleAndIngredient(recipe.getTitle(), ingredient);
            if (ingredientResponse.isEmpty()) { //add new ingredient to recipe
                recipeEntity = new Recipe();
                recipeEntity.setTitle(recipe.getTitle());
                recipeEntity.setIngredient(ingredient);
                recipeEntity = recipeRepository.save(recipeEntity);
            } 
        }
        return recipeEntity;
    } 

    // public Recipe saveRecipe(RecipeDto recipe) {
    //     List<Recipe> recipeResponse = recipeRepository.findByTitle(recipe.getTitle());
    //     Recipe recipeEntity = new Recipe();
    //     if (recipeResponse.isEmpty()) { //add new recipe as it doesn't exist
    //         recipeEntity.setTitle(recipe.getTitle());
    //     } else {
    //         recipeEntity = recipeResponse.get(0);
    //     }
    //     for (String ingredient : recipe.getIngredients()) { //save a new record per ingredient
    //         List<Recipe> ingredientResponse = recipeRepository
    //             .findByTitleAndIngredient(recipeEntity.getTitle(), ingredient);
    //         if (ingredientResponse.isEmpty()) { //add new ingredient to recipe
    //             Recipe recipeIngredientEntity = new Recipe();
    //             recipeIngredientEntity.setTitle(recipeEntity.getTitle());
    //             recipeIngredientEntity.setIngredient(ingredient);
    //             recipeIngredientEntity = recipeRepository.save(recipeIngredientEntity);
    //         } 
    //     }
    //     return recipeEntity;
    // }   


}
