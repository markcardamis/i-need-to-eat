package com.majoapps.lunchapp.business.service;

import java.util.List;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import com.majoapps.lunchapp.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.Data;

@Data
@Service
public class IngredientClientService {

    final String ROOT_URI = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    RecipeRepository recipeRepository;

    public Iterable<Ingredient> get() throws Exception {
        return ingredientRepository.findAll();
    }

    public List<IngredientDto> getIngredients() throws Exception {
        return restTemplate.getForObject(ROOT_URI, IngredientDtoWrapper.class).getIngredients();
    }

    public List<IngredientDto> saveIngredients() throws Exception {
        List<IngredientDto> ingredients = getIngredients();
        
        if (ingredients == null || ingredients.size() == 0) {
            throw new ResourceNotFoundException("Cannot retrieve any ingredients");
        } else {
            // iterate and save each object if it doesn't already exist
            ingredients.forEach(ingredient -> {
                List<Ingredient> ingredientResponse = ingredientRepository.findByTitle(ingredient.getTitle());
                Ingredient ingredientEntity = new Ingredient();
                if (ingredientResponse.size() == 0) { //add new ingredient as it doesn't exist
                
                    ingredientEntity.setTitle(ingredient.getTitle());
                    ingredientEntity.setBestBefore(ingredient.getBestBefore());
                    ingredientEntity.setUseBy(ingredient.getUseBy());
                    ingredientRepository.save(ingredientEntity);
                } else for (Ingredient ingredientTemp : ingredientResponse) { // update Dates on existing items
                    ingredientEntity.setId(ingredientTemp.getId());
                    ingredientEntity.setTitle(ingredientTemp.getTitle());
                    ingredientEntity.setRecipe(ingredientTemp.getRecipe());
                    ingredientEntity.setBestBefore(ingredient.getBestBefore());
                    ingredientEntity.setUseBy(ingredient.getUseBy());
                    ingredientRepository.save(ingredientEntity);
                }
                // if (recipeTitle != null) {
                //     List<Recipe> recipeResponse = recipeRepository.findByTitle(recipeTitle);
                //     if (recipeResponse.size() == 0) {
                //         //add new recipe as it doesn't exist
                //         Recipe recipeEntity = new Recipe();
                //         recipeEntity.setTitle(recipeTitle);
                //         recipeEntity = recipeRepository.save(recipeEntity);
                //         ingredientEntity.setRecipe(recipeEntity); 
                //     } else {
                //         //add to existing recipe
                //         ingredientEntity.setRecipe(recipeResponse.get(0));                    
                //     }   
                // }
                //ingredientRepository.save(ingredientEntity);
            });
        }
        return ingredients;
    }   

}
