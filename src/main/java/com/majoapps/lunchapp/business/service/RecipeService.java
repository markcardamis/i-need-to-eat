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

    public List<Recipe> findByTitle(String title) {
        return recipeRepository.findByTitle(title);
    }

    public List<Recipe> findByIngredient(String ingredient) {
        return recipeRepository.findByIngredient(ingredient);
    }
    
}
